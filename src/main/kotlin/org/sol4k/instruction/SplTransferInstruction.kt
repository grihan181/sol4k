package org.sol4k.instruction

import org.sol4k.AccountMeta
import org.sol4k.Binary
import org.sol4k.Constants.TOKEN_PROGRAM_ID
import org.sol4k.PublicKey
import java.io.ByteArrayOutputStream

class SplTransferInstruction(
    from: PublicKey,
    to: PublicKey,
    mint: PublicKey,
    owner: PublicKey,
    signers: List<PublicKey>,
    private val amount: Long,
    private val decimals: Int,
) : Instruction {
    companion object {
        @Suppress("unused")
        private const val instructionTransfer = 3
        private const val instructionTransferChecked = 12
    }

    override val data: ByteArray
        get() {
            ByteArrayOutputStream().use { buffer ->
                buffer.write(instructionTransferChecked)
                buffer.write(Binary.int64(amount))
                buffer.write(decimals)
                return buffer.toByteArray()
            }
        }

    override val keys: List<AccountMeta> = listOf(
        AccountMeta.writable(from),
        AccountMeta(mint),
        AccountMeta.writable(to),
        if (signers.isEmpty()) AccountMeta.signer(owner) else AccountMeta(owner),
        *signers.map { s -> AccountMeta.signer(s) }.toTypedArray()
    )

    override val programId: PublicKey = TOKEN_PROGRAM_ID
}
