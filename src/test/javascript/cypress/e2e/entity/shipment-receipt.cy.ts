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

describe('ShipmentReceipt e2e test', () => {
  const shipmentReceiptPageUrl = '/shipment-receipt';
  let username: string;
  let password: string;
  const shipmentReceiptSample = { quantity: 9058.22 };

  let shipmentReceipt;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-receipts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-receipts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-receipts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shipmentReceipt) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-receipts/${shipmentReceipt.id}`,
      }).then(() => {
        shipmentReceipt = undefined;
      });
    }
  });

  it('ShipmentReceipts menu should load ShipmentReceipts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-receipt');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentReceipt').should('exist');
    cy.location('pathname').should('eq', shipmentReceiptPageUrl);
  });

  describe('ShipmentReceipt page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentReceiptPageUrl);
      cy.getEntityHeading('ShipmentReceipt').should('not.contain', 'snpApp.shipmentReceipt.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentReceiptPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentReceipt page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentReceiptPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentReceipt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentReceiptPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-receipts',
          body: shipmentReceiptSample,
        }).then(({ body }) => {
          shipmentReceipt = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-receipts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shipment-receipts?page=0&size=20>; rel="last",<http://localhost/api/shipment-receipts?page=0&size=20>; rel="first"',
              },
              body: [shipmentReceipt],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentReceiptPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentReceipt page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentReceipt');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentReceiptPageUrl);
      });

      it('edit button click should load edit ShipmentReceipt page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentReceipt');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentReceiptPageUrl);
      });

      it('edit button click should load edit ShipmentReceipt page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentReceipt');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentReceiptPageUrl);
      });

      it('last delete button click should delete instance of ShipmentReceipt', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentReceipt').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentReceiptPageUrl);

        shipmentReceipt = undefined;
      });
    });
  });

  describe('new ShipmentReceipt page', () => {
    beforeEach(() => {
      cy.visit(shipmentReceiptPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentReceipt');
    });

    it('should create an instance of ShipmentReceipt', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('18656');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '18656');

      cy.get(`[data-cy="quantity"]`).type('29905.97');
      cy.get(`[data-cy="quantity"]`).should('have.value', '29905.97');

      cy.get(`[data-cy="quantityAccepted"]`).type('6885.59');
      cy.get(`[data-cy="quantityAccepted"]`).should('have.value', '6885.59');

      cy.get(`[data-cy="quantityRejected"]`).type('4709.24');
      cy.get(`[data-cy="quantityRejected"]`).should('have.value', '4709.24');

      cy.get(`[data-cy="receivedDate"]`).type('2023-12-14T13:53');
      cy.get(`[data-cy="receivedDate"]`).blur();
      cy.get(`[data-cy="receivedDate"]`).should('have.value', '2023-12-14T13:53');

      cy.get(`[data-cy="message"]`).type('snoop');
      cy.get(`[data-cy="message"]`).should('have.value', 'snoop');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentReceipt = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentReceiptPageUrl);
    });
  });
});
