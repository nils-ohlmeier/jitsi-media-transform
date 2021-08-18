package org.jitsi.nlj.transform.node.outgoing

import org.jitsi.nlj.PacketInfo
import org.jitsi.nlj.rtp.RtpExtensionType
import org.jitsi.nlj.transform.node.ModifierNode
import org.jitsi.nlj.util.StreamInformationStore
import org.jitsi.rtp.rtp.RtpPacket
import org.jitsi.rtp.rtp.header_extensions.SdesHeaderExtension
import org.jitsi.utils.logging2.Logger
import org.jitsi.utils.logging2.createChildLogger

class MidSenderNode(
    streamInformationStore: StreamInformationStore,
    parentLogger: Logger
) : ModifierNode("MID sender") {
    private var midExtId: Int? = null
    private var midValue: String? = null
    private val logger = createChildLogger(parentLogger)
    private val streamInfo = streamInformationStore

    init {
        // logger.error("MID Sender Node: init called")
        streamInfo.onRtpExtensionMapping(RtpExtensionType.MEDIA_IDENTIFICATION) {
            midExtId = it
        }
    }

    override fun modify(packetInfo: PacketInfo): PacketInfo {
        // logger.error("MID Sender Node: modify() called")
        midExtId?.let { midId ->
            logger.error("MID Sender Node ${hashCode()}: midExtId is set to $midId")
            val rtpPacket = packetInfo.packetAs<RtpPacket>()
            midValue = streamInfo.getMidBySsrc(rtpPacket.ssrc)
            if (midValue.isNullOrEmpty()) {
                logger.error("${hashCode()} missing MID value for SSRC ${rtpPacket.ssrc}")
                logger.error("${hashCode()} MID Association store: ${streamInfo.dumpMidAssociationStore()}")
                return packetInfo
            }
            val ext = rtpPacket.getHeaderExtension(midId)
                ?: rtpPacket.addHeaderExtension(midId, midValue!!.length)
            SdesHeaderExtension.setTextValue(ext, midValue!!)
            logger.error("MID Sender Node ${hashCode()}: MID value set to $midValue")
        }
        return packetInfo
    }

    override fun trace(f: () -> Unit) = f.invoke()
}
