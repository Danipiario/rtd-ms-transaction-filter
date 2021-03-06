package it.gov.pagopa.rtd.transaction_filter.batch.mapper;

import it.gov.pagopa.rtd.transaction_filter.batch.model.InboundTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of {@link FieldSetMapper}, to be used for a reader
 * related to files containing {@link InboundTransaction} data
 */

@RequiredArgsConstructor
public class InboundTransactionFieldSetMapper implements FieldSetMapper<InboundTransaction> {

    private final String timestampParser;

    /**
     *
     * @param fieldSet
     *          instance of FieldSet containing fields related to an {@link InboundTransaction}
     * @return instance of  {@link InboundTransaction}, mapped from a FieldSet
     * @throws BindException
     */
    @Override
    public InboundTransaction mapFieldSet(FieldSet fieldSet) throws BindException {

        if (fieldSet == null) {
            return null;
        }

        DateTimeFormatter dtf = timestampParser != null && !timestampParser.isEmpty() ?
                DateTimeFormatter.ofPattern(timestampParser).withZone(ZoneId.systemDefault()): null;

        InboundTransaction inboundTransaction =
                InboundTransaction.builder()
                        .acquirerCode(fieldSet.readString("codice_acquirer"))
                        .operationType(fieldSet.readString("tipo_operazione"))
                        .circuitType(fieldSet.readString("tipo_circuito"))
                        .pan(fieldSet.readString("PAN"))
                        .trxDate(dtf != null ?
                                ZonedDateTime.parse(fieldSet.readString("timestamp"), dtf).toOffsetDateTime() :
                                OffsetDateTime.parse(fieldSet.readString("timestamp")))
                        .idTrxAcquirer(fieldSet.readString("id_trx_acquirer"))
                        .idTrxIssuer(fieldSet.readString("id_trx_issuer"))
                        .correlationId(fieldSet.readString("correlation_id"))
                        .amount(fieldSet.readBigDecimal("importo"))
                        .amountCurrency(fieldSet.readString("currency"))
                        .acquirerId(fieldSet.readString("acquirerID"))
                        .merchantId(fieldSet.readString("merchantID"))
                        .mcc(fieldSet.readString("MCC"))
                        .build();

        return inboundTransaction;

    }

}
