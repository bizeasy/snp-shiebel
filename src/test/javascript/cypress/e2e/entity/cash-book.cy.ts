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

describe('CashBook e2e test', () => {
  const cashBookPageUrl = '/cash-book';
  let username: string;
  let password: string;
  const cashBookSample = { transactionDate: '2023-12-14', amount: 14443.25 };

  let cashBook;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/transaction-types',
      body: { name: 'aboard bah curiously', description: 'before' },
    }).then(({ body }) => {
      transactionType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cash-books+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cash-books').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cash-books/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transaction-types', {
      statusCode: 200,
      body: [transactionType],
    });

    cy.intercept('GET', '/api/transaction-categories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-method-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/departments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (cashBook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cash-books/${cashBook.id}`,
      }).then(() => {
        cashBook = undefined;
      });
    }
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

  it('CashBooks menu should load CashBooks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cash-book');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CashBook').should('exist');
    cy.location('pathname').should('eq', cashBookPageUrl);
  });

  describe('CashBook page', () => {
    it('should have translated page title', () => {
      cy.visit(cashBookPageUrl);
      cy.getEntityHeading('CashBook').should('not.contain', 'snpApp.cashBook.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cashBookPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CashBook page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cashBookPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CashBook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cash-books',
          body: {
            ...cashBookSample,
            transactionType,
          },
        }).then(({ body }) => {
          cashBook = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cash-books+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cash-books?page=0&size=20>; rel="last",<http://localhost/api/cash-books?page=0&size=20>; rel="first"',
              },
              body: [cashBook],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cashBookPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CashBook page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cashBook');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookPageUrl);
      });

      it('edit button click should load edit CashBook page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CashBook');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookPageUrl);
      });

      it('edit button click should load edit CashBook page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CashBook');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookPageUrl);
      });

      it('last delete button click should delete instance of CashBook', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cashBook').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookPageUrl);

        cashBook = undefined;
      });
    });
  });

  describe('new CashBook page', () => {
    beforeEach(() => {
      cy.visit(cashBookPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CashBook');
    });

    it('should create an instance of CashBook', () => {
      cy.get(`[data-cy="transactionDate"]`).type('2023-12-14');
      cy.get(`[data-cy="transactionDate"]`).blur();
      cy.get(`[data-cy="transactionDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="dueDate"]`).type('2023-12-14');
      cy.get(`[data-cy="dueDate"]`).blur();
      cy.get(`[data-cy="dueDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="amount"]`).type('16688.34');
      cy.get(`[data-cy="amount"]`).should('have.value', '16688.34');

      cy.get(`[data-cy="description"]`).type('physically astride');
      cy.get(`[data-cy="description"]`).should('have.value', 'physically astride');

      cy.get(`[data-cy="vendorCustomerName"]`).type('bah');
      cy.get(`[data-cy="vendorCustomerName"]`).should('have.value', 'bah');

      cy.get(`[data-cy="invoiceNo"]`).type('coolly retention');
      cy.get(`[data-cy="invoiceNo"]`).should('have.value', 'coolly retention');

      cy.get(`[data-cy="invoiceDate"]`).type('2023-12-14');
      cy.get(`[data-cy="invoiceDate"]`).blur();
      cy.get(`[data-cy="invoiceDate"]`).should('have.value', '2023-12-14');

      cy.get(`[data-cy="purpose"]`).type('phooey animated');
      cy.get(`[data-cy="purpose"]`).should('have.value', 'phooey animated');

      cy.get(`[data-cy="remarks"]`).type('orderly beside cutover');
      cy.get(`[data-cy="remarks"]`).should('have.value', 'orderly beside cutover');

      cy.get(`[data-cy="project"]`).type('drat briefly fibre');
      cy.get(`[data-cy="project"]`).should('have.value', 'drat briefly fibre');

      cy.get(`[data-cy="usageUnits"]`).type('blah meanwhile');
      cy.get(`[data-cy="usageUnits"]`).should('have.value', 'blah meanwhile');

      cy.get(`[data-cy="recurringTransaction"]`).should('not.be.checked');
      cy.get(`[data-cy="recurringTransaction"]`).click();
      cy.get(`[data-cy="recurringTransaction"]`).should('be.checked');

      cy.get(`[data-cy="frequency"]`).select('FORTNIGHTLY');

      cy.get(`[data-cy="transactionType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cashBook = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cashBookPageUrl);
    });
  });
});
