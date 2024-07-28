package br.com.copyimagem.msmonthlypayment.core.domain.representations;

public record MonthlyPaymentRequest(Long customerId, String invoiceNumber, String ticketNumber) {
}
