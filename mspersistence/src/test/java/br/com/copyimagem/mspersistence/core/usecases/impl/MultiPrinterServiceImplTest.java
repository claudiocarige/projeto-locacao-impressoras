package br.com.copyimagem.mspersistence.core.usecases.impl;


import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.domain.enums.MachineStatus;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalArgumentException;
import br.com.copyimagem.mspersistence.core.exceptions.IllegalStateException;
import br.com.copyimagem.mspersistence.core.exceptions.NoSuchElementException;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.CustomerRepository;
import br.com.copyimagem.mspersistence.infra.persistence.repositories.MultiPrinterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static br.com.copyimagem.mspersistence.core.domain.builders.LegalPersonalCustomerBuilder.oneLegalPersonalCustomer;
import static br.com.copyimagem.mspersistence.core.domain.builders.MultiPrinterBuilder.oneMultiPrinter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MultiPrinterServiceImplTest {

    private MultiPrinter multiPrinter;

    private MultiPrinterDTO multiPrinterDTO;

    @Mock
    private MultiPrinterRepository multiPrinterRepository;

    @Mock
    private ConvertObjectToObjectDTOService convertObjectToObjectDTOService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private MultiPrinterServiceImpl multiPrinterServiceImpl;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks( this );
        startEntities();

    }

    @Test
    @DisplayName( "Should return MultiPrinter by Id" )
    void shouldReturnMultiPrinterById() {

        when( multiPrinterRepository.findById( 1 ) ).thenReturn( Optional.ofNullable( multiPrinter ) );
        when( convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter ) ).thenReturn( multiPrinterDTO );

        MultiPrinterDTO resultDTO = multiPrinterServiceImpl.findMultiPrinterById( 1 );
        assertNotNull( resultDTO );
        assertEquals( multiPrinterDTO, resultDTO );
        assertEquals( multiPrinterDTO.getSerialNumber(), resultDTO.getSerialNumber() );
        assertEquals( MultiPrinterDTO.class, resultDTO.getClass() );

    }

    @Test
    @DisplayName( "Should return NoSuchElementException when MultiPrinter not found" )
    void shouldReturnNoSuchElementExceptionWhenMultiPrinterNotFound() {

        when( multiPrinterRepository.findById( 1 ) ).thenReturn( Optional.empty() );

        String message = assertThrows( NoSuchElementException.class,
                                                () -> multiPrinterServiceImpl.findMultiPrinterById( 1 ) ).getMessage();
        assertEquals( "MultiPrint not found", message );

    }

    @Test
    @DisplayName( "Should return a list of MultiPrinter" )
    void shouldReturnAListOfMultiPrinter() {

        when( multiPrinterRepository.findAll() ).thenReturn( List.of( multiPrinter ) );
        when( convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter ) ).thenReturn( multiPrinterDTO );

        List< MultiPrinterDTO > multiPrinters = multiPrinterServiceImpl.findAllMultiPrinters();
        assertAll( "MultiPrinter",
                () -> assertNotNull( multiPrinters ),
                () -> assertEquals( 1, multiPrinters.size() ),
                () -> assertEquals( MultiPrinterDTO.class, multiPrinters.get( 0 ).getClass() ),
                () -> assertEquals( multiPrinterDTO, multiPrinters.get( 0 ) ) );

    }

    @Test
    @DisplayName( "Should return a list of MultiPrinter by Customer" )
    void shouldReturnAListOfMultiPrinterByCustomer() {

        when( multiPrinterRepository.findAllByCustomerId( 1L ) ).thenReturn( List.of( multiPrinter ) );
        when( convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter ) ).thenReturn( multiPrinterDTO );

        List< MultiPrinterDTO > multiPrinters =
                                             multiPrinterServiceImpl.findAllMultiPrintersByCustomerId( 1L );
        assertAll( "MultiPrinter",
                () -> assertNotNull( multiPrinters ),
                () -> assertEquals( 1, multiPrinters.size() ),
                () -> assertEquals( MultiPrinterDTO.class, multiPrinters.get( 0 ).getClass() ),
                () -> assertEquals( multiPrinterDTO, multiPrinters.get( 0 ) ) );

    }

    @Test
    @DisplayName( "Should save a MultiPrinter" )
    void shouldSaveAMultiPrinter() {

        when( multiPrinterRepository.save( multiPrinter ) ).thenReturn( multiPrinter );
        when( convertObjectToObjectDTOService.convertToMultiPrinter( multiPrinterDTO ) ).thenReturn( multiPrinter );
        when( convertObjectToObjectDTOService.convertToMultiPrinterDTO( multiPrinter ) ).thenReturn( multiPrinterDTO );

        MultiPrinterDTO resultDTO = multiPrinterServiceImpl.saveMultiPrinter( multiPrinterDTO );
        assertNotNull( resultDTO );
        assertEquals( multiPrinterDTO, resultDTO );
        assertEquals( multiPrinterDTO.getSerialNumber(), resultDTO.getSerialNumber() );
        assertEquals( MultiPrinterDTO.class, resultDTO.getClass() );

        verify( multiPrinterRepository, times( 1 ) )
                                                            .existsBySerialNumber( multiPrinterDTO.getSerialNumber() );

    }

    @Test
    @DisplayName("You should not save a MultiPrint")
    void shouldNotSaveAMultiPrinter(){
        multiPrinter.setCustomer(oneLegalPersonalCustomer().nowCustomerPJ());
        when(multiPrinterRepository.existsBySerialNumber(multiPrinterDTO.getSerialNumber())).thenReturn(true);
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        when(convertObjectToObjectDTOService.convertToMultiPrinter(multiPrinterDTO)).thenReturn(multiPrinter);
        when(multiPrinterRepository.save(multiPrinter)).thenReturn(multiPrinter);

        String message = assertThrows(IllegalArgumentException.class,
                () -> multiPrinterServiceImpl.saveMultiPrinter(multiPrinterDTO)).getMessage();
        assertEquals("Serial number already exists", message);
    }

    @Test
    @DisplayName("Should set up a client on a MultiPrinter")
    void shouldSetUpClientOnAMultiPrinter(){
        multiPrinterDTO.setCustomer_id(null);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        when(multiPrinterRepository.save(multiPrinter)).thenReturn(multiPrinter);
        when(customerRepository.findById(1L))
                                        .thenReturn(Optional.ofNullable(oneLegalPersonalCustomer().nowCustomerPJ()));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        when(convertObjectToObjectDTOService.convertToMultiPrinter(multiPrinterDTO)).thenReturn(multiPrinter);

        MultiPrinterDTO multiPrinterDto = multiPrinterServiceImpl.setUpClientOnAMultiPrinter(1, 1L);
        assertEquals(multiPrinterDTO, multiPrinterDto);
        assertEquals(multiPrinterDTO.getId(), multiPrinterDto.getId());
        assertEquals(MultiPrinterDTO.class, multiPrinterDto.getClass());
        assertEquals(multiPrinterDTO.getCustomer_id(), multiPrinterDto.getCustomer_id());
    }

    @Test
    @DisplayName("Should throw an exception with existing Custome")
    void shouldThrowAnExceptionWithExistingCustomerOnMultiPrinter(){
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        when(customerRepository.findById(1L))
                .thenReturn(Optional.ofNullable(oneLegalPersonalCustomer().nowCustomerPJ()));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        String message = assertThrows(IllegalArgumentException.class,
                () -> multiPrinterServiceImpl.setUpClientOnAMultiPrinter(1, 1L)).getMessage();
        assertEquals("This printer is already Customer.", message);
    }

    @Test
    @DisplayName("Should delete a MultiPrinter")
    void shouldDeleteAMultiPrinter(){
        multiPrinter.setCustomer(null);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        multiPrinterServiceImpl.deleteMultiPrinter(1);
        verify(multiPrinterRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should throw an exception with existing Custome")
    void shouldThrowAnExceptionWithExistingCustomer(){
        multiPrinter.setCustomer(oneLegalPersonalCustomer().nowCustomerPJ());
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        String message = assertThrows(IllegalArgumentException.class,
                                                () -> multiPrinterServiceImpl.deleteMultiPrinter(1)).getMessage();
        assertEquals("This printer cannot be deleted.", message);
    }

    @Test
    @DisplayName("Should delete Customer From Multiprinter")
    void shouldUnSetUpCustomerFromMultiPrinterById(){
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        MultiPrinterDTO resultDTO = multiPrinterServiceImpl.unSetUpCustomerFromMultiPrinterById(1);
        assertEquals(multiPrinterDTO, resultDTO);
        assertEquals(multiPrinterDTO.getId(), resultDTO.getId());
        assertEquals(MultiPrinterDTO.class, resultDTO.getClass());
        assertEquals( multiPrinterDTO.getMachineStatus(), MachineStatus.DISPONIVEL);
        assertNull(resultDTO.getCustomer_id());
    }

    @Test
    @DisplayName("Should SET Machine Status")
    void shouldSetMAchineStatus(){
        multiPrinterDTO.setMachineStatus(MachineStatus.MANUTENCAO);
        when(multiPrinterRepository.updateMachineStatusById(1, MachineStatus.MANUTENCAO)).thenReturn(1);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        MultiPrinterDTO multiPrinterDto = multiPrinterServiceImpl.setMachineStatus(1,"MANUTENCAO");
        assertEquals(multiPrinterDTO, multiPrinterDto);
        assertEquals(MachineStatus.MANUTENCAO, multiPrinterDto.getMachineStatus());
    }

    @Test
    @DisplayName("Should throw an exception with invalid status")
    void shouldThrowAnExceptionWithInvalidStatus(){
        String message = assertThrows( IllegalArgumentException.class,
                                () -> multiPrinterServiceImpl.setMachineStatus(1, "INVALIDO")).getMessage();
        assertEquals("Invalid Status: INVALIDO", message);
    }

    @Test
    @DisplayName("Should SET Impression Counter")
    void shouldSetImpressionCounter(){
        when(multiPrinterRepository
        .updateImpressionCounterByAttribute(1, 10000, "impressionCounterInitial")).thenReturn(1);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.ofNullable(multiPrinter));
        when(multiPrinterRepository.save(multiPrinter)).thenReturn(multiPrinter);
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);
        when(convertObjectToObjectDTOService.convertToMultiPrinter(multiPrinterDTO)).thenReturn(multiPrinter);

        MultiPrinterDTO multiPrinterDto = multiPrinterServiceImpl
                                        .setImpressionCounter(1, 10000, "impressionCounterInitial");

        assertEquals(multiPrinterDTO, multiPrinterDto);
        assertEquals(10000, multiPrinterDto.getImpressionCounterInitial());
    }

    @Test
    @DisplayName("Should throw an exception with no rows updated")
    void shouldThrowAnExceptionWithNoRowsUpdated(){
        when(multiPrinterRepository.
             updateImpressionCounterByAttribute(1, 20000, "impressionCounterNow")).thenReturn(0);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.of(multiPrinter));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> multiPrinterServiceImpl.setImpressionCounter(1, 20000, "impressionCounterNow")
        );

        assertEquals("No rows updated. Check the conditions and input values.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException exception for smaller impressionCounterNow")
    void shouldThrowAnExceptionForSmallerImpressionCounterNow(){
        when(multiPrinterRepository.
                updateImpressionCounterByAttribute(1, 1000, "impressionCounterNow")).thenReturn(0);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.of(multiPrinter));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> multiPrinterServiceImpl.setImpressionCounter(1, 1000, "impressionCounterNow")
        );

        assertEquals("The COUNTER value must be greater than the current value.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException exception for invalid attribute")
    void shouldThrowAnExceptionForInvalidAttribute(){
        when(multiPrinterRepository.
                updateImpressionCounterByAttribute(1, 1000, "invalidAttribute")).thenReturn(0);
        when(multiPrinterRepository.findById(1)).thenReturn(Optional.of(multiPrinter));
        when(convertObjectToObjectDTOService.convertToMultiPrinterDTO(multiPrinter)).thenReturn(multiPrinterDTO);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> multiPrinterServiceImpl.setImpressionCounter(1, 1000, "invalidAttribute")
        );

        assertEquals("Invalid attribute: invalidAttribute", exception.getMessage());
    }

    private void startEntities() {

        multiPrinter = oneMultiPrinter().now();
        multiPrinterDTO = oneMultiPrinter().nowDTO();

    }


}