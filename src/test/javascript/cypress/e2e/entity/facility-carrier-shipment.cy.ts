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

describe('FacilityCarrierShipment e2e test', () => {
  const facilityCarrierShipmentPageUrl = '/facility-carrier-shipment';
  let username: string;
  let password: string;
  const facilityCarrierShipmentSample = {};

  let facilityCarrierShipment;
  let facility;
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
      url: '/api/facilities',
      body: { name: 'somber', description: 'blissfully', facilitySize: 24332.17 },
    }).then(({ body }) => {
      facility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'brr',
        organisationShortName: 'meh',
        salutation: 'wrongly carpool mumm',
        firstName: 'Rickey',
        middleName: 'allocation',
        lastName: 'Kertzmann',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'sans mid smoothly',
        primaryEmail: 'dratX',
        isTemporaryPassword: false,
        logoImageUrl: 'phew',
        profileImageUrl: 'enchanting ugh',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-carrier-shipments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-carrier-shipments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-carrier-shipments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [facility],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shipment-method-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (facilityCarrierShipment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-carrier-shipments/${facilityCarrierShipment.id}`,
      }).then(() => {
        facilityCarrierShipment = undefined;
      });
    }
  });

  afterEach(() => {
    if (facility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facilities/${facility.id}`,
      }).then(() => {
        facility = undefined;
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

  it('FacilityCarrierShipments menu should load FacilityCarrierShipments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-carrier-shipment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityCarrierShipment').should('exist');
    cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
  });

  describe('FacilityCarrierShipment page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityCarrierShipmentPageUrl);
      cy.getEntityHeading('FacilityCarrierShipment').should('not.contain', 'snpApp.facilityCarrierShipment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityCarrierShipmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityCarrierShipment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityCarrierShipmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityCarrierShipment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-carrier-shipments',
          body: {
            ...facilityCarrierShipmentSample,
            facility,
            party,
          },
        }).then(({ body }) => {
          facilityCarrierShipment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-carrier-shipments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [facilityCarrierShipment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityCarrierShipmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityCarrierShipment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityCarrierShipment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
      });

      it('edit button click should load edit FacilityCarrierShipment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityCarrierShipment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
      });

      it('edit button click should load edit FacilityCarrierShipment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityCarrierShipment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
      });

      it('last delete button click should delete instance of FacilityCarrierShipment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityCarrierShipment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);

        facilityCarrierShipment = undefined;
      });
    });
  });

  describe('new FacilityCarrierShipment page', () => {
    beforeEach(() => {
      cy.visit(facilityCarrierShipmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityCarrierShipment');
    });

    it('should create an instance of FacilityCarrierShipment', () => {
      cy.get(`[data-cy="facility"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityCarrierShipment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityCarrierShipmentPageUrl);
    });
  });
});
