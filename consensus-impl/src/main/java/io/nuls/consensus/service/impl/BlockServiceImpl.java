package io.nuls.consensus.service.impl;

import io.nuls.consensus.cache.manager.block.BlockCacheManager;
import io.nuls.consensus.utils.ConsensusTool;
import io.nuls.core.chain.entity.Block;
import io.nuls.core.chain.entity.BlockHeader;
import io.nuls.core.chain.entity.Transaction;
import io.nuls.core.context.NulsContext;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.utils.log.Log;
import io.nuls.db.transactional.annotation.TransactionalAnnotation;
import io.nuls.db.entity.BlockHeaderPo;
import io.nuls.ledger.service.intf.LedgerService;

import java.util.List;

/**
 * @author Niels
 * @date 2017/12/11
 */
public class BlockServiceImpl implements io.nuls.consensus.service.intf.BlockService {

    private BlockStorageService blockStorageService = BlockStorageService.getInstance();
    private BlockCacheManager blockCacheManager = BlockCacheManager.getInstance();
    private LedgerService ledgerService = NulsContext.getInstance().getService(LedgerService.class);

    @Override
    public Block getGengsisBlock() {
        return blockStorageService.getBlock(0);
    }

    @Override
    public long getLocalHeight() {
        long height = blockCacheManager.getBestHeight();
        if (height == 0) {
            height = blockStorageService.getBestHeight();
        }
        return height;
    }

    @Override
    public Block getLocalBestBlock() {
        return getBlock(getLocalHeight());
    }

    @Override
    public BlockHeader getBlockHeader(long height) {
        BlockHeader header;
        if (height <= blockCacheManager.getStoredHeight()) {
            header = blockStorageService.getBlockHeader(height);
        } else {
            header = blockCacheManager.getBlockHeader(height);
        }
        return header;
    }

    @Override
    public Block getBlock(String hash) {
        Block block = blockCacheManager.getBlock(hash);
        if (null == block) {
            block = blockStorageService.getBlock(hash);
        }
        return block;
    }

    @Override
    public Block getBlock(long height) {
        Block block = blockCacheManager.getBlock(height);
        if (null == block) {
            block = blockStorageService.getBlock(height);
        }
        return block;
    }


    @Override
    @TransactionalAnnotation
    public void saveBlock(Block block) {
        for (int x = 0; x < block.getHeader().getTxCount(); x++) {
            Transaction tx = block.getTxs().get(x);
            tx.setBlockHash(block.getHeader().getHash());
            tx.setBlockHeight(block.getHeader().getHeight());
            try {
                ledgerService.commitTx(tx);
            } catch (Exception e) {
                Log.error(e);
                rollback(block.getTxs(), x);
                throw new NulsRuntimeException(e);
            }
        }
        blockStorageService.save(block.getHeader());
        ledgerService.saveTxList(block.getHeader().getHeight(),block.getHeader().getHash().getDigestHex(),block.getTxs());
    }


    @Override
    @TransactionalAnnotation
    public void rollbackBlock(long height) {
        Block block = this.getBlock(height);
        if (null == block) {
            return;
        }
        this.rollback(block.getTxs(), block.getTxs().size() - 1);
        blockStorageService.delete(block.getHeader().getHash().getDigestHex());
    }

    @Override
    public int getBlockCount(String address, long roundStart, long roundEnd) {
        return this.blockStorageService.getCount(address, roundStart, roundEnd);
    }

    private void rollback(List<Transaction> txs, int max) {
        for (int x = 0; x < max; x++) {
            Transaction tx = txs.get(x);
            try {
                ledgerService.rollbackTx(tx);
            } catch (NulsException e) {
                Log.error(e);
            }
        }

    }
}
