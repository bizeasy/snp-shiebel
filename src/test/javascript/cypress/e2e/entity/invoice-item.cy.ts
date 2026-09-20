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

describe('InvoiceItem e2e test', () => {
  const invoiceItemPageUrl = '/invoice-item';
  let username: string;
  let password: string;
  // const invoiceItemSample = {"quantity":22890.34};

  let invoiceItem;
  // let invoice;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/invoices',
      body: {"invoiceDate":"2023-12-14T15:21:33.680Z","dueDate":"2023-12-14T02:12:29.353Z","paidDate":"2023-12-14T15:40:39.103Z","referenceNumber":"almighty widow suffocate","invoiceMessage":"polarisation devise","externalId":"sensitize","entryDate":"2023-12-14T00:12:46.875Z","grandTotal":15026.7,"projectName":"whoa eventually down","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"monstrous","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      invoice = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/invoice-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoice-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoice-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/invoices', {
      statusCode: 200,
      body: [invoice],
    });

    cy.intercept('GET', '/api/invoice-item-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-features', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-facilities', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/inventory-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/tax-authority-rate-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/invoice-items', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (invoiceItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-items/${invoiceItem.id}`,
      }).then(() => {
        invoiceItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (invoice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoices/${invoice.id}`,
      }).then(() => {
        invoice = undefined;
      });
    }
  });
   */

  it('InvoiceItems menu should load InvoiceItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InvoiceItem').should('exist');
    cy.location('pathname').should('eq', invoiceItemPageUrl);
  });

  describe('InvoiceItem page', () => {
    it('should have translated page title', () => {
      cy.visit(invoiceItemPageUrl);
      cy.getEntityHeading('InvoiceItem').should('not.contain', 'snpApp.invoiceItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoiceItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InvoiceItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoiceItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('InvoiceItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoice-items',
          body: {
            ...invoiceItemSample,
            invoice: invoice,
          },
        }).then(({ body }) => {
          invoiceItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoice-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/invoice-items?page=0&size=20>; rel="last",<http://localhost/api/invoice-items?page=0&size=20>; rel="first"',
              },
              body: [invoiceItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoiceItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(invoiceItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InvoiceItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoiceItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemPageUrl);
      });

      it('edit button click should load edit InvoiceItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemPageUrl);
      });

      it('edit button click should load edit InvoiceItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InvoiceItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of InvoiceItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoiceItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoiceItemPageUrl);

        invoiceItem = undefined;
      });
    });
  });

  describe('new InvoiceItem page', () => {
    beforeEach(() => {
      cy.visit(invoiceItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InvoiceItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of InvoiceItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('26931');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '26931');

      cy.get(`[data-cy="quantity"]`).type('9832.43');
      cy.get(`[data-cy="quantity"]`).should('have.value', '9832.43');

      cy.get(`[data-cy="amount"]`).type('20792.09');
      cy.get(`[data-cy="amount"]`).should('have.value', '20792.09');

      cy.get(`[data-cy="cancelQuantity"]`).type('30027.59');
      cy.get(`[data-cy="cancelQuantity"]`).should('have.value', '30027.59');

      cy.get(`[data-cy="selectedAmount"]`).type('26502.4');
      cy.get(`[data-cy="selectedAmount"]`).should('have.value', '26502.4');

      cy.get(`[data-cy="unitPrice"]`).type('6198.75');
      cy.get(`[data-cy="unitPrice"]`).should('have.value', '6198.75');

      cy.get(`[data-cy="unitListPrice"]`).type('12380.85');
      cy.get(`[data-cy="unitListPrice"]`).should('have.value', '12380.85');

      cy.get(`[data-cy="isModifiedPrice"]`).should('not.be.checked');
      cy.get(`[data-cy="isModifiedPrice"]`).click();
      cy.get(`[data-cy="isModifiedPrice"]`).should('be.checked');

      cy.get(`[data-cy="estimatedShipDate"]`).type('2023-12-14T02:10');
      cy.get(`[data-cy="estimatedShipDate"]`).blur();
      cy.get(`[data-cy="estimatedShipDate"]`).should('have.value', '2023-12-14T02:10');

      cy.get(`[data-cy="estimatedDeliveryDate"]`).type('2023-12-14T12:31');
      cy.get(`[data-cy="estimatedDeliveryDate"]`).blur();
      cy.get(`[data-cy="estimatedDeliveryDate"]`).should('have.value', '2023-12-14T12:31');

      cy.get(`[data-cy="shipBeforeDate"]`).type('2023-12-14T08:57');
      cy.get(`[data-cy="shipBeforeDate"]`).blur();
      cy.get(`[data-cy="shipBeforeDate"]`).should('have.value', '2023-12-14T08:57');

      cy.get(`[data-cy="shipAfterDate"]`).type('2023-12-14T13:31');
      cy.get(`[data-cy="shipAfterDate"]`).blur();
      cy.get(`[data-cy="shipAfterDate"]`).should('have.value', '2023-12-14T13:31');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-14T12:00');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-14T12:00');

      cy.get(`[data-cy="attr1"]`).type('institute');
      cy.get(`[data-cy="attr1"]`).should('have.value', 'institute');

      cy.get(`[data-cy="attr2"]`).type('safeguard');
      cy.get(`[data-cy="attr2"]`).should('have.value', 'safeguard');

      cy.get(`[data-cy="attr3"]`).type('swath');
      cy.get(`[data-cy="attr3"]`).should('have.value', 'swath');

      cy.get(`[data-cy="invoice"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoiceItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoiceItemPageUrl);
    });
  });
});
