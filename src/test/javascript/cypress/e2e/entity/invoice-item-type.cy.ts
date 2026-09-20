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

describe('InvoiceItemType e2e test', () => {
  const invoiceItemTypePageUrl = '/invoice-item-type';
  let username: string;
  let password: string;
  const invoiceItemTypeSample = { name: 'sternly than past' };

  let invoiceItemType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-item-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-item-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-item-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (invoiceItemType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-item-types/${invoiceItemType.id}`,
      }).then(() => {
        invoiceItemType = undefined;
      });
    }
  });

  it('InvoiceItemTypes menu should load InvoiceItemTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-item-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceItemType').should('exist');
    cy.location('pathname').should('eq', invoiceItemTypePageUrl);
  });

  describe('InvoiceItemType page', () => {
    it('should have translated page title', () => {
      cy.visit(invoiceItemTypePageUrl);
      cy.getEntityHeading('InvoiceItemType').should('not.contain', 'snpApp.invoiceItemType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceItemTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceItemType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoiceItemTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InvoiceItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-item-types',
          body: invoiceItemTypeSample,
        }).then(({ body }) => {
          invoiceItemType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-item-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [invoiceItemType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceItemTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InvoiceItemType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceItemType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypePageUrl);
      });

      it('edit button click should load edit InvoiceItemType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItemType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypePageUrl);
      });

      it('edit button click should load edit InvoiceItemType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItemType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypePageUrl);
      });

      it('last delete button click should delete instance of InvoiceItemType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoiceItemType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypePageUrl);

        invoiceItemType = undefined;
      });
    });
  });

  describe('new InvoiceItemType page', () => {
    beforeEach(() => {
      cy.visit(invoiceItemTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceItemType');
    });

    it('should create an instance of InvoiceItemType', () => {
      cy.get(`[data-cy="name"]`).type('tomorrow');
      cy.get(`[data-cy="name"]`).should('have.value', 'tomorrow');

      cy.get(`[data-cy="description"]`).type('freight basic aw');
      cy.get(`[data-cy="description"]`).should('have.value', 'freight basic aw');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoiceItemType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoiceItemTypePageUrl);
    });
  });
});
