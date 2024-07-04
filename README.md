# Projeto COPY IMAGEM

## Objetivo do Projeto:

O objetivo principal do Projeto COPY IMAGEM é solucionar os desafios enfrentados no controle de
informações nas locações de impressoras pela empresa Copyimagem, visando uma gestão mais eficaz e transparente.
 Iremos desenvolver e implementar uma arquitetura de software robusta e escalável para a aplicação CopyImagem. Faremos uso de
tecnologias avançadas como Java, Spring Boot e PostgreSQL para garantir um sistema eficiente e seguro. Adotaremos a
prática de Desenvolvimento Orientado a Testes (TDD) para garantir a qualidade do código e a robustez das funcionalidades
implementadas.
Nosso foco primário é garantir a eficiência, segurança e manutenibilidade do sistema, proporcionando uma experiência
excepcional aos usuários finais. Estamos concentrados especialmente na gestão de
clientes, máquinas e mensalidades.

### 1. Visão Geral da Arquitetura

A arquitetura da aplicação seguirá o padrão composto de camadas distintas para facilitar a manutenção e escalabilidade.

#### 1. Camada CORE da aplicação:

- Utilizará o Spring MVC para lidar com as requisições HTTP.
- Autenticação e autorização serão gerenciadas pelo Spring Security, utilizando JWT para token de autenticação.
- A documentação da API será gerada automaticamente pelo Swagger/OpenAPI.

##### a. Camada de Negócios (usecases):

- Implementará a lógica de negócios da aplicação.
- Fará uso do Spring Boot para criar serviços transacionais.
- Fará Integração com o banco de dados PostgreSQL.

##### b. Camada de Modelo (domain):

- Esta camada será responsável por entidades de domínio e enumeradores.
- Representará as entidades do domínio usando anotações JPA.
- Onde ficarão os enumeradores.
- Lombok será utilizado para reduzir a verbosidade.

##### c. Camada de Exceções (exceptions):

- Ficará responsável pelo tratamento das exceções personalizadas da aplicação.

##### d. Camada DTO (representationDTO):

- Esta camada terá a responsabilidade de transferir os dados entre as camadas da aplicação, sobretudo quando são expostas para o externo.

#### 2. Camada INFRA da aplicação:

##### a. Camada de Configuração (config):

- Responsável por lidar com as configurações da aplicação.

##### b. Camada Adapter (adapter):


##### c. Camada de Controle (controllers):

- Será responsável por receber as entradas dos usuários, coordenar as ações necessárias e chamar os casos de uso (use cases) apropriados.

- Terá a responsabilidade de traduzir os dados entre os casos de uso da aplicação e os detalhes da implementação externa, com frameworks, bibliotecas, APIs ou sistemas.

##### c. Camada de Persistência (persistence):

- Utilizará JPA (Java Persistence API) para interagir com o banco de dados.
- Repositórios serão criados para realizar operações de persistências como: Create, Read, Update e Delete.

### 2. Diagramas:

## Diagrama de Classes:
    
```mermaid
classDiagram
class Customer {
<<abstract>>
- Long id
- String clientName
- String primaryEmail
- List~String~ emailList
- String phoneNumber
- String whatsapp
- String bankCode
- Address address
- FinancialSituation financialSituation
- byte payDay
- CustomerContract customerContract
- List~MultiPrinter~ multiPrinterList
- List~MonthlyPayment~ monthlyPaymentList
}

    class LegalPersonalCustomer {
        - String cnpj
    }

    class NaturalPersonCustomer {
        - String cpf
    }

    class Address {
        - Long id
        - String street
        - String number
        - String city
        - String state
        - String country
    }

    class CustomerContract {
        - Long id
        - Integer printingFranchise
        - Double monthlyAmount
        - Short contractTime
        - LocalDate startContract
        - PrinterType printerType
    }

    class MultiPrinter {
        - Integer id
        - String brand
        - String model
        - String serialNumber
        - Double machineValue
        - MachineStatus machineStatus
        - PrinterType printType
        - Integer impressionCounterInitial
        - Integer impressionCounterBefore
        - Integer impressionCounterNow
        - Integer printingFranchise
        - Double amountPrinter
        - Double monthlyPrinterAmount
        - Customer customer
    }

    class MonthlyPayment {
        - Long id
        - Integer monthPayment
        - Integer yearPayment
        - Integer quantityPrintsPB
        - Integer quantityPrintsColor
        - Integer printingFranchisePB
        - Integer printingFranchiseColor
        - String invoiceNumber
        - String ticketNumber
        - Double amountPrinter
        - Double monthlyAmount
        - Double excessValuePrintsPB
        - Double excessValuePrintsColor
        - Double rateExcessColorPrinting
        - Double rateExcessBlackAndWhitePrinting
        - LocalDate expirationDate
        - LocalDate paymentDate
        - PaymentStatus paymentStatus
        - Customer customer
    }

    Customer <|-- LegalPersonalCustomer
    Customer <|-- NaturalPersonCustomer
    Customer "1" *-- "n" Address
    Customer "1" *-- "1" CustomerContract
    Customer "1" --> "1..N" MultiPrinter
    Customer "1"*-- "1..N" MonthlyPayment
    MultiPrinter --> Customer
    MonthlyPayment --> Customer
```


- Diagrama de Componentes:
  - Diagrama de Sequência:

### 3. Tecnologias Utilizadas:

- **Java e Spring Boot**:
    - Versão do Java: 17
    - Versão do Spring Boot: 3.1.0

- **Gerenciador de dependências**:
    - Maven

- **Banco de Dados**:
    - PostgreSQL: Utilizado para armazenamento persistente.

- **Persistência**:
    - Spring Data JPA: Facilita a implementação da camada de acesso a dados.

- **Segurança**:
    - Spring Security: Gerenciamento de autenticação e autorização.
    - JWT: Token de autenticação seguro.

- **Documentação**:
    - Swagger/OpenAPI: Documentação automática da API.

- **Lombok**:
    - Para redução de boilerplate code.

- **Relatórios**:
    - Apache POI: Para geração de relatórios em Excel.

### 4. Regras de Negócio

- **Sistema de Login**:
    - O login deverá ser feito com nickname e senha.
    - Após login feito, deverá ser disponibilizado um Token para acesso à API.
    - O login será administrado pelo Spring Security.

- **Controle de Clientes**:
    - Atributos para cliente: (listados)

- **Controle de Máquinas**:
    - Atributos para máquina: (listados)

- **Controle de Mensalidades**:
    - Deverá listar todos os clientes e máquinas.
    - Poderão ser editados os clientes e máquinas.
    - Não deverá possibilitar modificar valores de mensalidades num prazo maior de que 40 dias do seu lançamento.
    - Não devem ser deletadas nenhuma informação de clientes, de mensalidades e nem de máquinas.

### 5. Padrões de Codificação e Convenções:

- **Padrões de Commits e Versionamento**:
    - (detalhado na seção)
- Exemplo: 
  - [ topico ] : "**descrição do commit**"
  - <span style="color:green; font-weight: bold"> [ addition ] : First commit</span>

  - **Tópicos de Commit:**
    1. addition / inclusion: Usado especificamente para adicionar novos elementos ao código, como atributos, métodos, classes, etc.
    2. feature: Usado para adicionar uma nova funcionalidade ao código.
    3. refactor: Usado para fazer alterações no código para melhorar sua estrutura, legibilidade ou desempenho sem alterar seu comportamento externo.
    4. enhancement: Usado para adicionar melhorias ou otimizações ao código existente.
    5. update: Usado quando você está atualizando ou modificando partes existentes do código.
    6. docs: Usado para fazer alterações na documentação do código, como adicionar ou corrigir comentários, atualizar documentação de API, etc.
    7. cleanup: Usado para realizar tarefas de limpeza no código, como remover código morto, otimizar imports, etc.
    8. fix: Usado para corrigir um bug existente no código.
    9. test: Usado para adicionar, modificar ou corrigir testes de unidade, testes de integração, etc.
    10. test refactor: Usado para informar alterações no código dos testes.
    11. build: Usado para modificações nos arquivos de Build.

### 6. Arquitetura do Projeto Backend:
    
- **src**
    - main
       - java
          - <span style="color:yellow; font-weight: bold">core</span>
              - <span style="color:blue; font-weight: bold">domain</span>
                  - entities
                    - Customer
                    - LegalPesonalCustomer
                    - NaturalPersonCustomer
                    - Address
                    - MonthlyPayment
                    - MultiPrinter
                  - enums
                      - FinancialSituation.java
                      - MachineStatus.java
                      - PaymentStatus
              - <span style="color:blue; font-weight: bold">dto</span>
                - CustomerResponseDTO
                - LegalPersonalCustomerDTO
                - NaturalPersonCustomrDTO
                - UpdateCustometDTO
              - <span style="color:red; font-weight: bold">exceptions</span>
                - GlobalExceptionHandler
                - DataIntegrityViolationException
                - IllegalArgumentException
                - NoSuchElementException
                - StandardError
              - <span style="color:blue; font-weight: bold">usecases</span>
                  - interfaces
                      - CustomerService.java
                      - LegalPersonalCustomerService.java
                      - NaturalPersonCustomerService.java
                  - impl
                      - CustomerServiceImpl.java
                      - LegalPersonalCustomerServiceImpl.java
                      - NaturalPersonCustomerServiceImpl.java
          - <span style="color:yellow; font-weight: bold">infra</span>
              - <span style="color:blue; font-weight: bold">controllers</span>
                  - CustomerController
                  - LegalPersonalController
                  - NaturalPersonController
              - <span style="color:blue; font-weight: bold">adaptors
                  - apiexterna
                      - ApiExternaAdapter.java
                      - ApiExternaAdapterInterface.java
                  - emailapi
                      - MensageriaAdapter.java
                      - MensageriaAdapterInterface.java
            - <span style="color:blue; font-weight: bold">config</span>
                - ModelMapperConfig
                - SecurityConfig.java
                - security
                    - JwtTokenProvider.java  // Provedor JWT
            - <span style="color:blue; font-weight: bold">persistence
                - repositorios
                    - AddressRepository.java
                    - CustomerRepository.java
                    - LegalPersonalCustomerRepository.java
                    - NaturalPersonCustomerRepository.java

- **Gerenciamento de Dependências**:
    - Utilizar uma ferramenta de gerenciamento de dependências, como Maven ou Gradle.
