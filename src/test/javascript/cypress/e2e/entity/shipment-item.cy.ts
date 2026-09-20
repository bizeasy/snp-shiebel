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

describe('ShipmentItem e2e test', () => {
  const shipmentItemPageUrl = '/shipment-item';
  let username: string;
  let password: string;
  const shipmentItemSample = { quantity: 13420.98 };

  let shipmentItem;
  let shipment;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/shipments',
      body: {
        estimatedShipDate: '2023-12-14T02:10:05.807Z',
        estimatedReadyDate: '2023-12-14T05:38:39.643Z',
        estimatedArrivalDate: '2023-12-14T11:46:57.280Z',
        estimatedShipCost: 1547.11,
      },
    }).then(({ body }) => {
      shipment = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-items/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [shipment],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (shipmentItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-items/${shipmentItem.id}`,
      }).then(() => {
        shipmentItem = undefined;
      });
    }
  });

  afterEach(() => {
    if (shipment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipments/${shipment.id}`,
      }).then(() => {
        shipment = undefined;
      });
    }
  });

  it('ShipmentItems menu should load ShipmentItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentItem').should('exist');
    cy.location('pathname').should('eq', shipmentItemPageUrl);
  });

  describe('ShipmentItem page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentItemPageUrl);
      cy.getEntityHeading('ShipmentItem').should('not.contain', 'snpApp.shipmentItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentItemPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-items',
          body: {
            ...shipmentItemSample,
            shipment,
          },
        }).then(({ body }) => {
          shipmentItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shipment-items?page=0&size=20>; rel="last",<http://localhost/api/shipment-items?page=0&size=20>; rel="first"',
              },
              body: [shipmentItem],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentItemPageUrl);
      });

      it('edit button click should load edit ShipmentItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentItemPageUrl);
      });

      it('edit button click should load edit ShipmentItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentItemPageUrl);
      });

      it('last delete button click should delete instance of ShipmentItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentItemPageUrl);

        shipmentItem = undefined;
      });
    });
  });

  describe('new ShipmentItem page', () => {
    beforeEach(() => {
      cy.visit(shipmentItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentItem');
    });

    it('should create an instance of ShipmentItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('21866');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '21866');

      cy.get(`[data-cy="quantity"]`).type('27710.53');
      cy.get(`[data-cy="quantity"]`).should('have.value', '27710.53');

      cy.get(`[data-cy="message"]`).type('across official dearest');
      cy.get(`[data-cy="message"]`).should('have.value', 'across official dearest');

      cy.get(`[data-cy="shipment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentItemPageUrl);
    });
  });
});
