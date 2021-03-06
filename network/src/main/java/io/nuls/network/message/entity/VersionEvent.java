package io.nuls.network.message.entity;

import io.nuls.core.chain.entity.BaseNulsData;
import io.nuls.core.constant.NulsConstant;
import io.nuls.core.context.NulsContext;
import io.nuls.core.crypto.VarInt;
import io.nuls.core.event.EventHeader;
import io.nuls.core.exception.NulsException;
import io.nuls.core.utils.io.NulsByteBuffer;
import io.nuls.core.utils.io.NulsOutputStreamBuffer;
import io.nuls.core.utils.log.Log;
import io.nuls.core.utils.str.StringUtils;
import io.nuls.network.constant.NetworkConstant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author vivi
 * @Date 2017.11.01
 */
public class VersionEvent extends io.nuls.core.event.BaseEvent {

    public static final short OWN_MAIN_VERSION = 1;

    public static final short OWN_SUB_VERSION = 1001;

    private long bestBlockHeight;

    private String bestBlockHash;

    private String nulsVersion;

    public VersionEvent() {
        super(NulsConstant.MODULE_ID_NETWORK, NetworkConstant.NETWORK_VERSION_EVENT);
    }

    public VersionEvent(long bestBlockHeight, String bestBlockHash) {
        this();
        this.bestBlockHeight = bestBlockHeight;
        this.bestBlockHash = bestBlockHash;
    }


    @Override
    public int size() {
        int s = 0;
        s += EventHeader.EVENT_HEADER_LENGTH;
        s += VarInt.sizeOf(bestBlockHeight);
        // put the bestBlockHash.length
        s += 1;
        if (!StringUtils.isBlank(bestBlockHash)) {
            try {
                s += bestBlockHash.getBytes(NulsContext.DEFAULT_ENCODING).length;
            } catch (UnsupportedEncodingException e) {
                Log.error(e);
            }
        }
        s += 1;
        if (!StringUtils.isBlank(nulsVersion)) {
            try {
                s += nulsVersion.getBytes(NulsContext.DEFAULT_ENCODING).length;
            } catch (UnsupportedEncodingException e) {
                Log.error(e);
            }
        }
        return s;
    }

    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        stream.writeNulsData(getHeader());
//        stream.writeShort(version.getVersion());
        stream.write(new VarInt(bestBlockHeight).encode());
        stream.writeString(bestBlockHash);
        stream.writeString(nulsVersion);
    }

    @Override
    protected void parse(NulsByteBuffer byteBuffer) throws NulsException {
        this.setHeader(byteBuffer.readNulsData(new EventHeader()));
//        version = new NulsVersion(byteBuffer.readShort());
        bestBlockHeight = byteBuffer.readVarInt();
        bestBlockHash = new String(byteBuffer.readByLengthByte());
        nulsVersion = new String(byteBuffer.readByLengthByte());
    }

    @Override
    protected BaseNulsData parseEventBody(NulsByteBuffer byteBuffer) throws NulsException {
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("versionData:{");
        buffer.append(getHeader().toString());
//        buffer.append("version:" + version.getStringVersion() + ", ");
        buffer.append("bestBlockHeight:" + bestBlockHeight + ", ");
        buffer.append("bestBlockHash:" + bestBlockHash + ", ");
        buffer.append("nulsVersion:" + nulsVersion + "}");

        return buffer.toString();
    }

    public long getBestBlockHeight() {
        return bestBlockHeight;
    }

    public void setBestBlockHeight(long bestBlockHeight) {
        this.bestBlockHeight = bestBlockHeight;
    }

    public String getBestBlockHash() {
        return bestBlockHash;
    }

    public void setBestBlockHash(String bestBlockHash) {
        this.bestBlockHash = bestBlockHash;
    }

    public String getNulsVersion() {
        return nulsVersion;
    }

    public void setNulsVersion(String nulsVersion) {
        this.nulsVersion = nulsVersion;
    }

}
