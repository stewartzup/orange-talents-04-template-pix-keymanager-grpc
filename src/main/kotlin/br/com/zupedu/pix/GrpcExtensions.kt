package br.com.zupedu.pix.registra

import br.com.zupedu.RegistraChavePixRequest
import br.com.zupedu.pix.TipoDeChave
import br.com.zupedu.pix.TipoDeConta
import br.com.zupedu.pix.registra.NovaChavePix


fun RegistraChavePixRequest.toModel() : NovaChavePix {
    return NovaChavePix(
        clienteId = clienteId,
        tipo = when (tipoDeChave) {
            br.com.zupedu.TipoDeChave.UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoDeChave.name)
        },
        chave = chave,
        tipoDeConta = when (tipoDeConta) {
            br.com.zupedu.TipoDeConta.UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoDeConta.name)
        }
    )
}