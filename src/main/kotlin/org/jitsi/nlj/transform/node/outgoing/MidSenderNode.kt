package org.jitsi.nlj.transform.node.outgoing

import org.jitsi.nlj.PacketInfo
import org.jitsi.nlj.rtp.RtpExtensionType
import org.jitsi.nlj.transform.node.ModifierNode
import org.jitsi.nlj.util.StreamInformationStore
import org.jitsi.utils.logging2.Logger
import org.jitsi.utils.logging2.createChildLogger

class MidSenderNode(
    streamInformationStore: StreamInformationStore,
    parentLogger: Logger
) : ModifierNode("MID sender") {
    private var midExtId: Int? = null
    private val logger = createChildLogger(parentLogger)
    private val streamInfo = streamInformationStore

    init {
        logger.error("MID Sender Node: init called")
        streamInformationStore.onRtpExtensionMapping(RtpExtensionType.MEDIA_IDENTIFICATION) {
            midExtId = it
        }
    }

    override fun modify(packetInfo: PacketInfo): PacketInfo {
        TODO("Not yet implemented")
    }

    override fun trace(f: () -> Unit) {
        TODO("Not yet implemented")
    }
}
