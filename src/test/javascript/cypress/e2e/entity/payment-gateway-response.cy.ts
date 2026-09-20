import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PaymentGatewayResponse e2e test', () => {
  const paymentGatewayResponsePageUrl = '/payment-gateway-response';
  let username: string;
  let password: string;
  const paymentGatewayResponseSample = {};

  let paymentGatewayResponse;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-gateway-responses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-gateway-responses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-gateway-responses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentGatewayResponse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-gateway-responses/${paymentGatewayResponse.id}`,
      }).then(() => {
        paymentGatewayResponse = undefined;
      });
    }
  });

  it('PaymentGatewayResponses menu should load PaymentGatewayResponses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-gateway-response');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentGatewayResponse').should('exist');
    cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
  });

  describe('PaymentGatewayResponse page', () => {
    it('should have translated page title', () => {
      cy.visit(paymentGatewayResponsePageUrl);
      cy.getEntityHeading('PaymentGatewayResponse').should('not.contain', 'snpApp.paymentGatewayResponse.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentGatewayResponsePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentGatewayResponse page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${paymentGatewayResponsePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PaymentGatewayResponse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-gateway-responses',
          body: paymentGatewayResponseSample,
        }).then(({ body }) => {
          paymentGatewayResponse = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-gateway-responses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/payment-gateway-responses?page=0&size=20>; rel="last",<http://localhost/api/payment-gateway-responses?page=0&size=20>; rel="first"',
              },
              body: [paymentGatewayResponse],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentGatewayResponsePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentGatewayResponse page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentGatewayResponse');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
      });

      it('edit button click should load edit PaymentGatewayResponse page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGatewayResponse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
      });

      it('edit button click should load edit PaymentGatewayResponse page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentGatewayResponse');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
      });

      it('last delete button click should delete instance of PaymentGatewayResponse', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentGatewayResponse').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);

        paymentGatewayResponse = undefined;
      });
    });
  });

  describe('new PaymentGatewayResponse page', () => {
    beforeEach(() => {
      cy.visit(paymentGatewayResponsePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaymentGatewayResponse');
    });

    it('should create an instance of PaymentGatewayResponse', () => {
      cy.get(`[data-cy="referenceNumber"]`).type('huzzah');
      cy.get(`[data-cy="referenceNumber"]`).should('have.value', 'huzzah');

      cy.get(`[data-cy="amount"]`).type('14309.05');
      cy.get(`[data-cy="amount"]`).should('have.value', '14309.05');

      cy.get(`[data-cy="gatewayMessage"]`).type('knottily');
      cy.get(`[data-cy="gatewayMessage"]`).should('have.value', 'knottily');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paymentGatewayResponse = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', paymentGatewayResponsePageUrl);
    });
  });
});
