package io.nuls.consensus.cache.manager.block;

import io.nuls.cache.util.CacheMap;
import io.nuls.consensus.constant.ConsensusCacheConstant;
import io.nuls.consensus.constant.PocConsensusConstant;
import io.nuls.core.chain.entity.Block;
import io.nuls.core.chain.entity.BlockHeader;
import io.nuls.core.chain.entity.SmallBlock;
import io.nuls.core.constant.ErrorCode;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.validate.ValidateResult;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Niels
 * @date 2017/12/12
 */
public class BlockCacheManager {
    private static final String HEIGHT_HASH_CACHE = "blocks-height-hash";
    private static final BlockCacheManager INSTANCE = new BlockCacheManager();
    private CacheMap<String, BlockHeader> headerCacheMap;
    private CacheMap<String, BlockHeader> tempHeaderCacheMap;
    private CacheMap<Long, Set<String>> blockHeightCacheMap;
    private CacheMap<String, Integer> hashConfirmedCountMap;

    private CacheMap<String, Block> blockCacheMap;
    private CacheMap<String, SmallBlock> smallBlockCacheMap;

    private long bestHeight;
    private long storedHeight;


    private BlockCacheManager() {
    }

    public static BlockCacheManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        smallBlockCacheMap = new CacheMap<>(ConsensusCacheConstant.SMALL_BLOCK_CACHE_NAME, 32, ConsensusCacheConstant.LIVE_TIME, 0);
        headerCacheMap = new CacheMap<>(ConsensusCacheConstant.BLOCK_HEADER_CACHE_NAME, 16, ConsensusCacheConstant.LIVE_TIME, 0);
        blockCacheMap = new CacheMap<>(ConsensusCacheConstant.BLOCK_CACHE_NAME, 64, ConsensusCacheConstant.LIVE_TIME, 0);
        tempHeaderCacheMap = new CacheMap<>(ConsensusCacheConstant.TEMP_BLOCK_HEADER_CACHE_NAME, 32, ConsensusCacheConstant.LIVE_TIME, 0);
        blockHeightCacheMap = new CacheMap<>(ConsensusCacheConstant.BLOCK_HEIGHT_CACHE_NAME, 16, ConsensusCacheConstant.LIVE_TIME, 0);
        hashConfirmedCountMap = new CacheMap<>(ConsensusCacheConstant.HASH_CONFIRMED_COUNT_CACHE, 16, ConsensusCacheConstant.LIVE_TIME, 0);
    }

    //todo
    public ValidateResult cacheBlockHeader(BlockHeader header) {
        long height = header.getHeight();
        boolean discard = true;
        if (height <= storedHeight) {
            discard = true;
        }else if (height <= bestHeight) {
            //todo 验证出块人、签名，如果签名正确，则处罚（分叉[red]、网络延时出块失败[yellow]）

        }












        if (discard) {
            return ValidateResult.getFailedResult("");
        }


        tempHeaderCacheMap.put(header.getHash().getDigestHex(), header);
        Integer confirmedCount = hashConfirmedCountMap.get(header.getPreHash().getDigestHex());
        if (null == confirmedCount) {
            confirmedCount = 0;
        }
        confirmedCount++;
        hashConfirmedCountMap.put(header.getPreHash().getDigestHex(), confirmedCount);
        Set<String> set = blockHeightCacheMap.get(height);
        if (null == set) {
            set = new HashSet<>();
        }
        set.add(header.getHash().getDigestHex());
        blockHeightCacheMap.put(height, set);
        checkNextBlockHeader(height);
        return ValidateResult.getSuccessResult();
    }

    private void checkNextBlockHeader(long height) {
        // todo auto-generated method stub(niels)
        // 检查下一个头，如果存在则验证并下载


    }

    public BlockHeader getBlockHeader(String hash) {
        return this.headerCacheMap.get(hash);
    }

    public ValidateResult cacheBlock(Block block) {
        ValidateResult result = block.verify();
        if (result.isFailed()) {
            return result;
        }
        blockCacheMap.put(block.getHeader().getHash().getDigestHex(), block);
        return ValidateResult.getSuccessResult();
    }

    public Block getBlock(String hash) {
        return blockCacheMap.get(hash);
    }

    public void cacheSmallBlock(SmallBlock smallBlock) {
        smallBlockCacheMap.put(smallBlock.getBlockHash().getDigestHex(), smallBlock);
    }

    public SmallBlock getSmallBlock(String hash) {
        return smallBlockCacheMap.get(hash);
    }

    public void clear() {
        this.blockCacheMap.clear();
        this.tempHeaderCacheMap.clear();
        this.headerCacheMap.clear();
        this.smallBlockCacheMap.clear();
    }

    public void destroy() {
        this.blockCacheMap.destroy();
        this.tempHeaderCacheMap.destroy();
        this.headerCacheMap.destroy();
        this.smallBlockCacheMap.destroy();
    }

    public void removeBlock(long height) {
        Set<String> hashset = blockHeightCacheMap.get(height);
        if (null == hashset) {
            return;
        }
        this.blockHeightCacheMap.remove(height);
        for (String hash : hashset) {
            this.blockCacheMap.remove(hash);
            this.smallBlockCacheMap.remove(hash);
            this.headerCacheMap.remove(hash);
            this.tempHeaderCacheMap.remove(hash);
        }
    }

    public long getBestHeight() {
        return bestHeight;
    }

    public Block getBlock(long height) {
        Set<String> hashset = blockHeightCacheMap.get(height);
        if (null == hashset || hashset.size() != 1) {
            return null;
        }
        for (String hash : hashset) {
            return getBlock(hash);
        }
        return null;
    }

    public long getStoredHeight() {
        return storedHeight;
    }

    public void setStoredHeight(long storedHeight) {
        this.storedHeight = storedHeight;
    }

    public Block getConfirmedBlock(long height) {
        Set<String> hashset = blockHeightCacheMap.get(height);
        if (null == hashset) {
            return null;
        }
        String rightBlockHash = null;
        int confirmedCount = 0;
        for (String hash : hashset) {
            int count = hashConfirmedCountMap.get(hash);
            if (count > confirmedCount) {
                rightBlockHash = hash;
                confirmedCount = count;
            }
        }
        if (confirmedCount <= PocConsensusConstant.CONFIRM_BLOCK_COUNT) {
            return null;
        }
        return blockCacheMap.get(rightBlockHash);
    }

    public BlockHeader getBlockHeader(long height) {
        Set<String> hashset = blockHeightCacheMap.get(height);
        if (null == hashset || hashset.size() != 1) {
            return null;
        }
        for (String hash : hashset) {
            return getBlockHeader(hash);
        }
        return null;
    }
}
