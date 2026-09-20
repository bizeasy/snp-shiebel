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

describe('TransactionType e2e test', () => {
  const transactionTypePageUrl = '/transaction-type';
  let username: string;
  let password: string;
  const transactionTypeSample = { name: 'ack spherical disrespect' };

  let transactionType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transactionType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-types/${transactionType.id}`,
      }).then(() => {
        transactionType = undefined;
      });
    }
  });

  it('TransactionTypes menu should load TransactionTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionType').should('exist');
    cy.location('pathname').should('eq', transactionTypePageUrl);
  });

  describe('TransactionType page', () => {
    it('should have translated page title', () => {
      cy.visit(transactionTypePageUrl);
      cy.getEntityHeading('TransactionType').should('not.contain', 'snpApp.transactionType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transactionTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransactionType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${transactionTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transaction-types',
          body: transactionTypeSample,
        }).then(({ body }) => {
          transactionType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transaction-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [transactionType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transactionTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TransactionType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionTypePageUrl);
      });

      it('edit button click should load edit TransactionType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionTypePageUrl);
      });

      it('edit button click should load edit TransactionType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransactionType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionTypePageUrl);
      });

      it('last delete button click should delete instance of TransactionType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transactionType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transactionTypePageUrl);

        transactionType = undefined;
      });
    });
  });

  describe('new TransactionType page', () => {
    beforeEach(() => {
      cy.visit(transactionTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TransactionType');
    });

    it('should create an instance of TransactionType', () => {
      cy.get(`[data-cy="name"]`).type('off pharmacopoeia');
      cy.get(`[data-cy="name"]`).should('have.value', 'off pharmacopoeia');

      cy.get(`[data-cy="description"]`).type('while orchid');
      cy.get(`[data-cy="description"]`).should('have.value', 'while orchid');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        transactionType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', transactionTypePageUrl);
    });
  });
});
