package br.com.copyimagem.mspersistence.core.usecases.impl;


import br.com.copyimagem.mspersistence.core.domain.entities.MultiPrinter;
import br.com.copyimagem.mspersistence.core.dtos.MultiPrinterDTO;
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

    private void startEntities() {

        multiPrinter = oneMultiPrinter().now();
        multiPrinterDTO = oneMultiPrinter().nowDTO();

    }


}