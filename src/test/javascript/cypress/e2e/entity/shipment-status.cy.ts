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

describe('ShipmentStatus e2e test', () => {
  const shipmentStatusPageUrl = '/shipment-status';
  let username: string;
  let password: string;
  const shipmentStatusSample = { statusDateTime: '2023-12-14T08:15:31.424Z' };

  let shipmentStatus;
  let shipment;
  let status;

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
        estimatedShipDate: '2023-12-14T13:40:15.507Z',
        estimatedReadyDate: '2023-12-13T23:43:57.804Z',
        estimatedArrivalDate: '2023-12-14T10:22:48.833Z',
        estimatedShipCost: 6036.15,
      },
    }).then(({ body }) => {
      shipment = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/statuses',
      body: { name: 'regarding toward', sequenceNo: 2263, description: 'owlishly yippee' },
    }).then(({ body }) => {
      status = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-statuses/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [shipment],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [status],
    });
  });

  afterEach(() => {
    if (shipmentStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-statuses/${shipmentStatus.id}`,
      }).then(() => {
        shipmentStatus = undefined;
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
    if (status) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/statuses/${status.id}`,
      }).then(() => {
        status = undefined;
      });
    }
  });

  it('ShipmentStatuses menu should load ShipmentStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentStatus').should('exist');
    cy.location('pathname').should('eq', shipmentStatusPageUrl);
  });

  describe('ShipmentStatus page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentStatusPageUrl);
      cy.getEntityHeading('ShipmentStatus').should('not.contain', 'snpApp.shipmentStatus.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentStatus page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentStatusPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentStatusPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-statuses',
          body: {
            ...shipmentStatusSample,
            shipment,
            status,
          },
        }).then(({ body }) => {
          shipmentStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shipment-statuses?page=0&size=20>; rel="last",<http://localhost/api/shipment-statuses?page=0&size=20>; rel="first"',
              },
              body: [shipmentStatus],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentStatus');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentStatusPageUrl);
      });

      it('edit button click should load edit ShipmentStatus page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentStatusPageUrl);
      });

      it('edit button click should load edit ShipmentStatus page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentStatus');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentStatusPageUrl);
      });

      it('last delete button click should delete instance of ShipmentStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentStatusPageUrl);

        shipmentStatus = undefined;
      });
    });
  });

  describe('new ShipmentStatus page', () => {
    beforeEach(() => {
      cy.visit(shipmentStatusPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentStatus');
    });

    it('should create an instance of ShipmentStatus', () => {
      cy.get(`[data-cy="statusDateTime"]`).type('2023-12-14T14:22');
      cy.get(`[data-cy="statusDateTime"]`).blur();
      cy.get(`[data-cy="statusDateTime"]`).should('have.value', '2023-12-14T14:22');

      cy.get(`[data-cy="sequenceNo"]`).type('28028');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '28028');

      cy.get(`[data-cy="shipment"]`).select(1);
      cy.get(`[data-cy="status"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentStatus = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentStatusPageUrl);
    });
  });
});
