package org.jitsi.nlj.util

import org.jitsi.utils.MediaType

class MidAssociationStore {
    // FIXME the map needs to contain a list of Long's
    // private val midAssociations: MutableMap<String, Long> = mutableMapOf()

    private val midAssociationBySsrc: MutableMap<Long, String> = mutableMapOf()

    private var audioMid: String? = null
    private var audioSsrc: Long? = null
    private var videoMid: String? = null
    private var videoSsrc: Long? = null

    fun addMidAssociation(midAssociation: MidAssociation) {
        midAssociationBySsrc.put(midAssociation.ssrc, midAssociation.mid)
    }

    private fun addLocalMids() {
        if (audioMid != null && audioSsrc != null) {
            addMidAssociation(MidAssociation(audioMid!!, audioSsrc!!))
        }
        if (videoMid != null && videoSsrc != null) {
            addMidAssociation(MidAssociation(videoMid!!, videoSsrc!!))
        }
    }

    fun addMediaTypeMid(type: MediaType, mid: String) {
        when (type) {
            MediaType.AUDIO -> audioMid = mid
            MediaType.VIDEO -> videoMid = mid
        }
        addLocalMids()
    }

    fun addLocalSsrc(type: MediaType, ssrc: Long) {
        when (type) {
            MediaType.AUDIO -> audioSsrc = ssrc
            MediaType.VIDEO -> videoSsrc = ssrc
        }
        addLocalMids()
    }

    fun getMidbySsrc(ssrc: Long): String? =
        midAssociationBySsrc.get(ssrc)

    override fun toString(): String = midAssociationBySsrc.toString()
}
