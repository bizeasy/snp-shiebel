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

describe('FacilityLocation e2e test', () => {
  const facilityLocationPageUrl = '/facility-location';
  let username: string;
  let password: string;
  const facilityLocationSample = { locationSeqId: 'before tousle' };

  let facilityLocation;
  let facility;

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
      body: { name: 'likewise', description: 'um modulo stock', facilitySize: 2286.76 },
    }).then(({ body }) => {
      facility = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-locations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-locations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-locations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [facility],
    });

    cy.intercept('GET', '/api/location-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (facilityLocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-locations/${facilityLocation.id}`,
      }).then(() => {
        facilityLocation = undefined;
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
  });

  it('FacilityLocations menu should load FacilityLocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-location');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityLocation').should('exist');
    cy.location('pathname').should('eq', facilityLocationPageUrl);
  });

  describe('FacilityLocation page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityLocationPageUrl);
      cy.getEntityHeading('FacilityLocation').should('not.contain', 'snpApp.facilityLocation.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityLocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityLocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityLocationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityLocationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-locations',
          body: {
            ...facilityLocationSample,
            facility,
          },
        }).then(({ body }) => {
          facilityLocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-locations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/facility-locations?page=0&size=20>; rel="last",<http://localhost/api/facility-locations?page=0&size=20>; rel="first"',
              },
              body: [facilityLocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityLocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityLocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityLocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityLocationPageUrl);
      });

      it('edit button click should load edit FacilityLocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityLocationPageUrl);
      });

      it('edit button click should load edit FacilityLocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityLocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityLocationPageUrl);
      });

      it('last delete button click should delete instance of FacilityLocation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityLocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityLocationPageUrl);

        facilityLocation = undefined;
      });
    });
  });

  describe('new FacilityLocation page', () => {
    beforeEach(() => {
      cy.visit(facilityLocationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityLocation');
    });

    it('should create an instance of FacilityLocation', () => {
      cy.get(`[data-cy="locationSeqId"]`).type('omelet jump');
      cy.get(`[data-cy="locationSeqId"]`).should('have.value', 'omelet jump');

      cy.get(`[data-cy="areaId"]`).type('wildly wherever');
      cy.get(`[data-cy="areaId"]`).should('have.value', 'wildly wherever');

      cy.get(`[data-cy="aisleId"]`).type('jump blah');
      cy.get(`[data-cy="aisleId"]`).should('have.value', 'jump blah');

      cy.get(`[data-cy="sectionId"]`).type('incidentally optimis');
      cy.get(`[data-cy="sectionId"]`).should('have.value', 'incidentally optimis');

      cy.get(`[data-cy="levelId"]`).type('despite boldly');
      cy.get(`[data-cy="levelId"]`).should('have.value', 'despite boldly');

      cy.get(`[data-cy="positionId"]`).type('blissfully boo');
      cy.get(`[data-cy="positionId"]`).should('have.value', 'blissfully boo');

      cy.get(`[data-cy="facility"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityLocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityLocationPageUrl);
    });
  });
});
