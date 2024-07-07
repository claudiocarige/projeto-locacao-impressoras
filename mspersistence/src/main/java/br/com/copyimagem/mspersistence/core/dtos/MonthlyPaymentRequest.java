package br.com.copyimagem.mspersistence.core.dtos;

public record MonthlyPaymentRequest(Long customerId, String invoiceNumber, String ticketNumber) {
}
