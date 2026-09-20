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

describe('InvoiceType e2e test', () => {
  const invoiceTypePageUrl = '/invoice-type';
  let username: string;
  let password: string;
  const invoiceTypeSample = { name: 'affiliate superficial aboard' };

  let invoiceType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (invoiceType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-types/${invoiceType.id}`,
      }).then(() => {
        invoiceType = undefined;
      });
    }
  });

  it('InvoiceTypes menu should load InvoiceTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceType').should('exist');
    cy.location('pathname').should('eq', invoiceTypePageUrl);
  });

  describe('InvoiceType page', () => {
    it('should have translated page title', () => {
      cy.visit(invoiceTypePageUrl);
      cy.getEntityHeading('InvoiceType').should('not.contain', 'snpApp.invoiceType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoiceTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InvoiceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-types',
          body: invoiceTypeSample,
        }).then(({ body }) => {
          invoiceType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [invoiceType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InvoiceType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceTypePageUrl);
      });

      it('edit button click should load edit InvoiceType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceTypePageUrl);
      });

      it('edit button click should load edit InvoiceType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceTypePageUrl);
      });

      it('last delete button click should delete instance of InvoiceType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoiceType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceTypePageUrl);

        invoiceType = undefined;
      });
    });
  });

  describe('new InvoiceType page', () => {
    beforeEach(() => {
      cy.visit(invoiceTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceType');
    });

    it('should create an instance of InvoiceType', () => {
      cy.get(`[data-cy="name"]`).type('questioningly impolite colorfully');
      cy.get(`[data-cy="name"]`).should('have.value', 'questioningly impolite colorfully');

      cy.get(`[data-cy="description"]`).type('tomorrow repeatedly');
      cy.get(`[data-cy="description"]`).should('have.value', 'tomorrow repeatedly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoiceType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoiceTypePageUrl);
    });
  });
});
