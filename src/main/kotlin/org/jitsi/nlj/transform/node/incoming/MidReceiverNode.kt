/*
 * Copyright @ 2018 - Present, 8x8 Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.nlj.transform.node.incoming

import org.jitsi.nlj.PacketInfo
import org.jitsi.nlj.rtp.RtpExtensionType
import org.jitsi.nlj.transform.node.ObserverNode
import org.jitsi.nlj.util.StreamInformationStoreImpl
import org.jitsi.rtp.rtp.RtpPacket
import org.jitsi.rtp.rtp.header_extensions.SdesHeaderExtension
import org.jitsi.utils.logging2.Logger
import org.jitsi.utils.logging2.createChildLogger

class MidReceiverNode(
    streamInformationStore: StreamInformationStoreImpl,
    parentLogger: Logger
) : ObserverNode("MID Receiver") {
    private var midExtId: Int? = null
    private val logger = createChildLogger(parentLogger)
    private val streamInfo = streamInformationStore

    init {
        logger.error("MID Receiver Node: init called")
        streamInformationStore.onRtpExtensionMapping(RtpExtensionType.MEDIA_IDENTIFICATION) {
            midExtId = it
        }
    }

    override fun observe(packetInfo: PacketInfo) {
        // logger.error("MID Receiver Node: observe() called")
        midExtId?.let { midId ->
            // logger.error("MID Receiver Node: midExtId is set to $midId")
            val rtpPacket = packetInfo.packetAs<RtpPacket>()
            rtpPacket.getHeaderExtension(midId)?.let { ext ->
                streamInfo.receivedMid = true
                val midValue = SdesHeaderExtension.getTextValue(ext)
                logger.error("Found MID value $midValue in RTP packet.")
            }
        }
    }

    override fun trace(f: () -> Unit) = f.invoke()
}
