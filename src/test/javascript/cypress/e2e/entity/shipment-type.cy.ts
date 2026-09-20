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

describe('ShipmentType e2e test', () => {
  const shipmentTypePageUrl = '/shipment-type';
  let username: string;
  let password: string;
  const shipmentTypeSample = { name: 'deliberately' };

  let shipmentType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shipmentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-types/${shipmentType.id}`,
      }).then(() => {
        shipmentType = undefined;
      });
    }
  });

  it('ShipmentTypes menu should load ShipmentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentType').should('exist');
    cy.location('pathname').should('eq', shipmentTypePageUrl);
  });

  describe('ShipmentType page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentTypePageUrl);
      cy.getEntityHeading('ShipmentType').should('not.contain', 'snpApp.shipmentType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-types',
          body: shipmentTypeSample,
        }).then(({ body }) => {
          shipmentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shipmentType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentTypePageUrl);
      });

      it('edit button click should load edit ShipmentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentTypePageUrl);
      });

      it('edit button click should load edit ShipmentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentTypePageUrl);
      });

      it('last delete button click should delete instance of ShipmentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentTypePageUrl);

        shipmentType = undefined;
      });
    });
  });

  describe('new ShipmentType page', () => {
    beforeEach(() => {
      cy.visit(shipmentTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentType');
    });

    it('should create an instance of ShipmentType', () => {
      cy.get(`[data-cy="name"]`).type('flat rue bide');
      cy.get(`[data-cy="name"]`).should('have.value', 'flat rue bide');

      cy.get(`[data-cy="description"]`).type('dispose');
      cy.get(`[data-cy="description"]`).should('have.value', 'dispose');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentTypePageUrl);
    });
  });
});
