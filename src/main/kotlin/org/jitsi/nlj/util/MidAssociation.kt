package org.jitsi.nlj.util

class MidAssociation(
    val mid: String,
    val ssrc: Long
) {
    override fun toString(): String = "$mid -> $ssrc"
}
