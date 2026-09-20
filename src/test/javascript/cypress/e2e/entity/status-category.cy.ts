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

describe('StatusCategory e2e test', () => {
  const statusCategoryPageUrl = '/status-category';
  let username: string;
  let password: string;
  const statusCategorySample = { name: 'pinion' };

  let statusCategory;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/status-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/status-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/status-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (statusCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/status-categories/${statusCategory.id}`,
      }).then(() => {
        statusCategory = undefined;
      });
    }
  });

  it('StatusCategories menu should load StatusCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StatusCategory').should('exist');
    cy.location('pathname').should('eq', statusCategoryPageUrl);
  });

  describe('StatusCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(statusCategoryPageUrl);
      cy.getEntityHeading('StatusCategory').should('not.contain', 'snpApp.statusCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StatusCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${statusCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('StatusCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/status-categories',
          body: statusCategorySample,
        }).then(({ body }) => {
          statusCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/status-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [statusCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StatusCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('statusCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusCategoryPageUrl);
      });

      it('edit button click should load edit StatusCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusCategoryPageUrl);
      });

      it('edit button click should load edit StatusCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusCategoryPageUrl);
      });

      it('last delete button click should delete instance of StatusCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('statusCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', statusCategoryPageUrl);

        statusCategory = undefined;
      });
    });
  });

  describe('new StatusCategory page', () => {
    beforeEach(() => {
      cy.visit(statusCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StatusCategory');
    });

    it('should create an instance of StatusCategory', () => {
      cy.get(`[data-cy="name"]`).type('absentmindedly opera');
      cy.get(`[data-cy="name"]`).should('have.value', 'absentmindedly opera');

      cy.get(`[data-cy="description"]`).type('obediently marimba');
      cy.get(`[data-cy="description"]`).should('have.value', 'obediently marimba');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        statusCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', statusCategoryPageUrl);
    });
  });
});
