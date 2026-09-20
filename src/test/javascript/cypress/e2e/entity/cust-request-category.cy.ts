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

describe('CustRequestCategory e2e test', () => {
  const custRequestCategoryPageUrl = '/cust-request-category';
  let username: string;
  let password: string;
  const custRequestCategorySample = { name: 'dicker bah' };

  let custRequestCategory;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (custRequestCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-categories/${custRequestCategory.id}`,
      }).then(() => {
        custRequestCategory = undefined;
      });
    }
  });

  it('CustRequestCategories menu should load CustRequestCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestCategory').should('exist');
    cy.location('pathname').should('eq', custRequestCategoryPageUrl);
  });

  describe('CustRequestCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestCategoryPageUrl);
      cy.getEntityHeading('CustRequestCategory').should('not.contain', 'snpApp.custRequestCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-categories',
          body: custRequestCategorySample,
        }).then(({ body }) => {
          custRequestCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [custRequestCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustRequestCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestCategoryPageUrl);
      });

      it('edit button click should load edit CustRequestCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestCategoryPageUrl);
      });

      it('edit button click should load edit CustRequestCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestCategoryPageUrl);
      });

      it('last delete button click should delete instance of CustRequestCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestCategoryPageUrl);

        custRequestCategory = undefined;
      });
    });
  });

  describe('new CustRequestCategory page', () => {
    beforeEach(() => {
      cy.visit(custRequestCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestCategory');
    });

    it('should create an instance of CustRequestCategory', () => {
      cy.get(`[data-cy="name"]`).type('gaseous shovel');
      cy.get(`[data-cy="name"]`).should('have.value', 'gaseous shovel');

      cy.get(`[data-cy="description"]`).type('abaft worth drat');
      cy.get(`[data-cy="description"]`).should('have.value', 'abaft worth drat');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestCategoryPageUrl);
    });
  });
});
