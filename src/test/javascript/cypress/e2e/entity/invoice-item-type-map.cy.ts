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

describe('InvoiceItemTypeMap e2e test', () => {
  const invoiceItemTypeMapPageUrl = '/invoice-item-type-map';
  let username: string;
  let password: string;
  const invoiceItemTypeMapSample = { invoiceItemMapKey: 'knowledgeably athwart brace' };

  let invoiceItemTypeMap;
  let invoiceType;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/invoice-types',
      body: { name: 'quizzically yuck violently', description: 'lady voluntarily tool' },
    }).then(({ body }) => {
      invoiceType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/invoice-item-types',
      body: { name: 'boohoo than', description: 'instead before' },
    }).then(({ body }) => {
      invoiceItemType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-item-type-maps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-item-type-maps').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-item-type-maps/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/invoice-types', {
      statusCode: 200,
      body: [invoiceType],
    });

    cy.intercept('GET', '/api/invoice-item-types', {
      statusCode: 200,
      body: [invoiceItemType],
    });
  });

  afterEach(() => {
    if (invoiceItemTypeMap) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-item-type-maps/${invoiceItemTypeMap.id}`,
      }).then(() => {
        invoiceItemTypeMap = undefined;
      });
    }
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
    if (invoiceItemType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-item-types/${invoiceItemType.id}`,
      }).then(() => {
        invoiceItemType = undefined;
      });
    }
  });

  it('InvoiceItemTypeMaps menu should load InvoiceItemTypeMaps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-item-type-map');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceItemTypeMap').should('exist');
    cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
  });

  describe('InvoiceItemTypeMap page', () => {
    it('should have translated page title', () => {
      cy.visit(invoiceItemTypeMapPageUrl);
      cy.getEntityHeading('InvoiceItemTypeMap').should('not.contain', 'snpApp.invoiceItemTypeMap.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceItemTypeMapPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceItemTypeMap page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoiceItemTypeMapPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InvoiceItemTypeMap');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-item-type-maps',
          body: {
            ...invoiceItemTypeMapSample,
            invoiceType,
            invoiceItemType,
          },
        }).then(({ body }) => {
          invoiceItemTypeMap = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-item-type-maps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [invoiceItemTypeMap],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceItemTypeMapPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InvoiceItemTypeMap page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceItemTypeMap');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
      });

      it('edit button click should load edit InvoiceItemTypeMap page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItemTypeMap');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
      });

      it('edit button click should load edit InvoiceItemTypeMap page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItemTypeMap');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
      });

      it('last delete button click should delete instance of InvoiceItemTypeMap', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoiceItemTypeMap').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);

        invoiceItemTypeMap = undefined;
      });
    });
  });

  describe('new InvoiceItemTypeMap page', () => {
    beforeEach(() => {
      cy.visit(invoiceItemTypeMapPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceItemTypeMap');
    });

    it('should create an instance of InvoiceItemTypeMap', () => {
      cy.get(`[data-cy="invoiceItemMapKey"]`).type('petty favorable gah');
      cy.get(`[data-cy="invoiceItemMapKey"]`).should('have.value', 'petty favorable gah');

      cy.get(`[data-cy="invoiceType"]`).select(1);
      cy.get(`[data-cy="invoiceItemType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoiceItemTypeMap = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoiceItemTypeMapPageUrl);
    });
  });
});
