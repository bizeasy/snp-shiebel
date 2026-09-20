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

describe('ShipmentMethodType e2e test', () => {
  const shipmentMethodTypePageUrl = '/shipment-method-type';
  let username: string;
  let password: string;
  const shipmentMethodTypeSample = { name: 'towards birdcage' };

  let shipmentMethodType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-method-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-method-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-method-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shipmentMethodType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-method-types/${shipmentMethodType.id}`,
      }).then(() => {
        shipmentMethodType = undefined;
      });
    }
  });

  it('ShipmentMethodTypes menu should load ShipmentMethodTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-method-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentMethodType').should('exist');
    cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
  });

  describe('ShipmentMethodType page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentMethodTypePageUrl);
      cy.getEntityHeading('ShipmentMethodType').should('not.contain', 'snpApp.shipmentMethodType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentMethodTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentMethodType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentMethodTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentMethodType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-method-types',
          body: shipmentMethodTypeSample,
        }).then(({ body }) => {
          shipmentMethodType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-method-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shipmentMethodType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentMethodTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentMethodType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentMethodType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
      });

      it('edit button click should load edit ShipmentMethodType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentMethodType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
      });

      it('edit button click should load edit ShipmentMethodType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentMethodType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
      });

      it('last delete button click should delete instance of ShipmentMethodType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentMethodType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentMethodTypePageUrl);

        shipmentMethodType = undefined;
      });
    });
  });

  describe('new ShipmentMethodType page', () => {
    beforeEach(() => {
      cy.visit(shipmentMethodTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentMethodType');
    });

    it('should create an instance of ShipmentMethodType', () => {
      cy.get(`[data-cy="name"]`).type('past');
      cy.get(`[data-cy="name"]`).should('have.value', 'past');

      cy.get(`[data-cy="description"]`).type('helpfully subexpression over');
      cy.get(`[data-cy="description"]`).should('have.value', 'helpfully subexpression over');

      cy.get(`[data-cy="sequenceNo"]`).type('25654');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '25654');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentMethodType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentMethodTypePageUrl);
    });
  });
});
