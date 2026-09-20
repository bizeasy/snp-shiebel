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

describe('FacilityContactMech e2e test', () => {
  const facilityContactMechPageUrl = '/facility-contact-mech';
  let username: string;
  let password: string;
  const facilityContactMechSample = { fromDate: '2023-12-13T22:20:35.127Z' };

  let facilityContactMech;
  let facility;
  let contactMech;

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
      body: { name: 'or provided', description: 'voluntarily liberalize ownership', facilitySize: 19749.28 },
    }).then(({ body }) => {
      facility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/contact-meches',
      body: { infoString: 'boulevard ad' },
    }).then(({ body }) => {
      contactMech = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-contact-meches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-contact-meches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-contact-meches/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [facility],
    });

    cy.intercept('GET', '/api/contact-meches', {
      statusCode: 200,
      body: [contactMech],
    });

    cy.intercept('GET', '/api/contact-mech-purposes', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (facilityContactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-contact-meches/${facilityContactMech.id}`,
      }).then(() => {
        facilityContactMech = undefined;
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
    if (contactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-meches/${contactMech.id}`,
      }).then(() => {
        contactMech = undefined;
      });
    }
  });

  it('FacilityContactMeches menu should load FacilityContactMeches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-contact-mech');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityContactMech').should('exist');
    cy.location('pathname').should('eq', facilityContactMechPageUrl);
  });

  describe('FacilityContactMech page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityContactMechPageUrl);
      cy.getEntityHeading('FacilityContactMech').should('not.contain', 'snpApp.facilityContactMech.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityContactMechPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityContactMech page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityContactMechPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityContactMechPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-contact-meches',
          body: {
            ...facilityContactMechSample,
            facility,
            contactMech,
          },
        }).then(({ body }) => {
          facilityContactMech = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-contact-meches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/facility-contact-meches?page=0&size=20>; rel="last",<http://localhost/api/facility-contact-meches?page=0&size=20>; rel="first"',
              },
              body: [facilityContactMech],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityContactMechPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityContactMech page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityContactMech');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityContactMechPageUrl);
      });

      it('edit button click should load edit FacilityContactMech page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityContactMechPageUrl);
      });

      it('edit button click should load edit FacilityContactMech page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityContactMech');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityContactMechPageUrl);
      });

      it('last delete button click should delete instance of FacilityContactMech', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityContactMech').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityContactMechPageUrl);

        facilityContactMech = undefined;
      });
    });
  });

  describe('new FacilityContactMech page', () => {
    beforeEach(() => {
      cy.visit(facilityContactMechPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityContactMech');
    });

    it('should create an instance of FacilityContactMech', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T08:17');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T08:17');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T07:35');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T07:35');

      cy.get(`[data-cy="facility"]`).select(1);
      cy.get(`[data-cy="contactMech"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityContactMech = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityContactMechPageUrl);
    });
  });
});
