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

describe('Facility e2e test', () => {
  const facilityPageUrl = '/facility';
  let username: string;
  let password: string;
  const facilitySample = { name: 'fly' };

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
    cy.intercept('GET', '/api/facilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facilities/*').as('deleteEntityRequest');
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

  it('Facilities menu should load Facilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Facility').should('exist');
    cy.location('pathname').should('eq', facilityPageUrl);
  });

  describe('Facility page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityPageUrl);
      cy.getEntityHeading('Facility').should('not.contain', 'snpApp.facility.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Facility page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Facility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facilities',
          body: facilitySample,
        }).then(({ body }) => {
          facility = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/facilities?page=0&size=20>; rel="last",<http://localhost/api/facilities?page=0&size=20>; rel="first"',
              },
              body: [facility],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Facility page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facility');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPageUrl);
      });

      it('edit button click should load edit Facility page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Facility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPageUrl);
      });

      it('edit button click should load edit Facility page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Facility');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPageUrl);
      });

      it('last delete button click should delete instance of Facility', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facility').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityPageUrl);

        facility = undefined;
      });
    });
  });

  describe('new Facility page', () => {
    beforeEach(() => {
      cy.visit(facilityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Facility');
    });

    it('should create an instance of Facility', () => {
      cy.get(`[data-cy="name"]`).type('fooey corny');
      cy.get(`[data-cy="name"]`).should('have.value', 'fooey corny');

      cy.get(`[data-cy="description"]`).type('for');
      cy.get(`[data-cy="description"]`).should('have.value', 'for');

      cy.get(`[data-cy="facilitySize"]`).type('9638.17');
      cy.get(`[data-cy="facilitySize"]`).should('have.value', '9638.17');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facility = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityPageUrl);
    });
  });
});
