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

describe('TransactionCategory e2e test', () => {
  const transactionCategoryPageUrl = '/transaction-category';
  let username: string;
  let password: string;
  const transactionCategorySample = { name: 'hence' };

  let transactionCategory;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transactionCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-categories/${transactionCategory.id}`,
      }).then(() => {
        transactionCategory = undefined;
      });
    }
  });

  it('TransactionCategories menu should load TransactionCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionCategory').should('exist');
    cy.location('pathname').should('eq', transactionCategoryPageUrl);
  });

  describe('TransactionCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(transactionCategoryPageUrl);
      cy.getEntityHeading('TransactionCategory').should('not.contain', 'snpApp.transactionCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${transactionCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TransactionCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-categories',
          body: transactionCategorySample,
        }).then(({ body }) => {
          transactionCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TransactionCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionCategoryPageUrl);
      });

      it('edit button click should load edit TransactionCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionCategoryPageUrl);
      });

      it('edit button click should load edit TransactionCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionCategoryPageUrl);
      });

      it('last delete button click should delete instance of TransactionCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionCategoryPageUrl);

        transactionCategory = undefined;
      });
    });
  });

  describe('new TransactionCategory page', () => {
    beforeEach(() => {
      cy.visit(transactionCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TransactionCategory');
    });

    it('should create an instance of TransactionCategory', () => {
      cy.get(`[data-cy="name"]`).type('seriously');
      cy.get(`[data-cy="name"]`).should('have.value', 'seriously');

      cy.get(`[data-cy="description"]`).type('unconscious distinct');
      cy.get(`[data-cy="description"]`).should('have.value', 'unconscious distinct');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        transactionCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', transactionCategoryPageUrl);
    });
  });
});
