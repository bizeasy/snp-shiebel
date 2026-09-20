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

describe('FacilityType e2e test', () => {
  const facilityTypePageUrl = '/facility-type';
  let username: string;
  let password: string;
  const facilityTypeSample = { name: 'finally jealously incidentally' };

  let facilityType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (facilityType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-types/${facilityType.id}`,
      }).then(() => {
        facilityType = undefined;
      });
    }
  });

  it('FacilityTypes menu should load FacilityTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityType').should('exist');
    cy.location('pathname').should('eq', facilityTypePageUrl);
  });

  describe('FacilityType page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityTypePageUrl);
      cy.getEntityHeading('FacilityType').should('not.contain', 'snpApp.facilityType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-types',
          body: facilityTypeSample,
        }).then(({ body }) => {
          facilityType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [facilityType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityTypePageUrl);
      });

      it('edit button click should load edit FacilityType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityTypePageUrl);
      });

      it('edit button click should load edit FacilityType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityTypePageUrl);
      });

      it('last delete button click should delete instance of FacilityType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityTypePageUrl);

        facilityType = undefined;
      });
    });
  });

  describe('new FacilityType page', () => {
    beforeEach(() => {
      cy.visit(facilityTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityType');
    });

    it('should create an instance of FacilityType', () => {
      cy.get(`[data-cy="name"]`).type('besides before');
      cy.get(`[data-cy="name"]`).should('have.value', 'besides before');

      cy.get(`[data-cy="description"]`).type('forecast exaggerate');
      cy.get(`[data-cy="description"]`).should('have.value', 'forecast exaggerate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityTypePageUrl);
    });
  });
});
