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

describe('CarrierShipmentMethod e2e test', () => {
  const carrierShipmentMethodPageUrl = '/carrier-shipment-method';
  let username: string;
  let password: string;
  const carrierShipmentMethodSample = {};

  let carrierShipmentMethod;
  let shipmentMethodType;
  let party;

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
      url: '/api/shipment-method-types',
      body: { name: 'blah even nearly', description: 'meanwhile er', sequenceNo: 13947 },
    }).then(({ body }) => {
      shipmentMethodType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'seagull',
        organisationShortName: 'deliberately fowl',
        salutation: 'mmm traffic hmph',
        firstName: 'Louise',
        middleName: 'readjust attest father',
        lastName: 'Flatley',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'indeed pro',
        primaryEmail: 'unlike',
        isTemporaryPassword: true,
        logoImageUrl: 'gadzooks jovially',
        profileImageUrl: 'amused accept',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/carrier-shipment-methods+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/carrier-shipment-methods').as('postEntityRequest');
    cy.intercept('DELETE', '/api/carrier-shipment-methods/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shipment-method-types', {
      statusCode: 200,
      body: [shipmentMethodType],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (carrierShipmentMethod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/carrier-shipment-methods/${carrierShipmentMethod.id}`,
      }).then(() => {
        carrierShipmentMethod = undefined;
      });
    }
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
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('CarrierShipmentMethods menu should load CarrierShipmentMethods page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('carrier-shipment-method');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarrierShipmentMethod').should('exist');
    cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
  });

  describe('CarrierShipmentMethod page', () => {
    it('should have translated page title', () => {
      cy.visit(carrierShipmentMethodPageUrl);
      cy.getEntityHeading('CarrierShipmentMethod').should('not.contain', 'snpApp.carrierShipmentMethod.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carrierShipmentMethodPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarrierShipmentMethod page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${carrierShipmentMethodPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CarrierShipmentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/carrier-shipment-methods',
          body: {
            ...carrierShipmentMethodSample,
            shipmentMethodType,
            party,
          },
        }).then(({ body }) => {
          carrierShipmentMethod = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/carrier-shipment-methods+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [carrierShipmentMethod],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carrierShipmentMethodPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarrierShipmentMethod page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carrierShipmentMethod');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
      });

      it('edit button click should load edit CarrierShipmentMethod page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarrierShipmentMethod');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
      });

      it('edit button click should load edit CarrierShipmentMethod page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarrierShipmentMethod');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
      });

      it('last delete button click should delete instance of CarrierShipmentMethod', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('carrierShipmentMethod').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);

        carrierShipmentMethod = undefined;
      });
    });
  });

  describe('new CarrierShipmentMethod page', () => {
    beforeEach(() => {
      cy.visit(carrierShipmentMethodPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CarrierShipmentMethod');
    });

    it('should create an instance of CarrierShipmentMethod', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('19524');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '19524');

      cy.get(`[data-cy="carrierServiceCode"]`).type('an');
      cy.get(`[data-cy="carrierServiceCode"]`).should('have.value', 'an');

      cy.get(`[data-cy="shipmentMethodType"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        carrierShipmentMethod = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', carrierShipmentMethodPageUrl);
    });
  });
});
