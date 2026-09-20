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

describe('FacilityParty e2e test', () => {
  const facilityPartyPageUrl = '/facility-party';
  let username: string;
  let password: string;
  const facilityPartySample = { fromDate: '2023-12-14T08:22:49.886Z' };

  let facilityParty;
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
      body: { name: 'wavy', description: 'whose postbox whose', facilitySize: 24667.01 },
    }).then(({ body }) => {
      facility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'along desecrate shinny',
        organisationShortName: 'whether',
        salutation: 'while who',
        firstName: 'Laura',
        middleName: 'considering briskly',
        lastName: 'Gleichner',
        gender: 'OTHER',
        birthDate: '2023-12-13',
        primaryPhone: 'aha playfully hunger',
        primaryEmail: 'formamide flame',
        isTemporaryPassword: false,
        logoImageUrl: 'afore',
        profileImageUrl: 'never',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-parties+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-parties').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-parties/*').as('deleteEntityRequest');
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
  });

  afterEach(() => {
    if (facilityParty) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-parties/${facilityParty.id}`,
      }).then(() => {
        facilityParty = undefined;
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

  it('FacilityParties menu should load FacilityParties page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-party');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityParty').should('exist');
    cy.location('pathname').should('eq', facilityPartyPageUrl);
  });

  describe('FacilityParty page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityPartyPageUrl);
      cy.getEntityHeading('FacilityParty').should('not.contain', 'snpApp.facilityParty.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityPartyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityParty page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityPartyPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityParty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPartyPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-parties',
          body: {
            ...facilityPartySample,
            facility,
            party,
          },
        }).then(({ body }) => {
          facilityParty = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-parties+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/facility-parties?page=0&size=20>; rel="last",<http://localhost/api/facility-parties?page=0&size=20>; rel="first"',
              },
              body: [facilityParty],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityPartyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityParty page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityParty');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPartyPageUrl);
      });

      it('edit button click should load edit FacilityParty page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityParty');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPartyPageUrl);
      });

      it('edit button click should load edit FacilityParty page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityParty');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPartyPageUrl);
      });

      it('last delete button click should delete instance of FacilityParty', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityParty').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPartyPageUrl);

        facilityParty = undefined;
      });
    });
  });

  describe('new FacilityParty page', () => {
    beforeEach(() => {
      cy.visit(facilityPartyPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityParty');
    });

    it('should create an instance of FacilityParty', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T15:53');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T15:53');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-13T22:03');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-13T22:03');

      cy.get(`[data-cy="facility"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityParty = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityPartyPageUrl);
    });
  });
});
