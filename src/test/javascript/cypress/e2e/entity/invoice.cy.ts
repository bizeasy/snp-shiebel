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

describe('Invoice e2e test', () => {
  const invoicePageUrl = '/invoice';
  let username: string;
  let password: string;
  const invoiceSample = { invoiceDate: '2023-12-14T12:18:26.603Z' };

  let invoice;
  let invoiceType;
  let party;

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
      body: { name: 'sharply', description: 'fine' },
    }).then(({ body }) => {
      invoiceType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'revoke early cap',
        organisationShortName: 'atrium wisecrack tromp',
        salutation: 'laughter despite nor',
        firstName: 'Berta',
        middleName: 'glittering fluffy',
        lastName: 'Harber',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'poorly seriously pro',
        primaryEmail: 'tinderbox jealous',
        isTemporaryPassword: true,
        logoImageUrl: 'upon',
        profileImageUrl: 'divert all',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoices/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/invoice-types', {
      statusCode: 200,
      body: [invoiceType],
    });

    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/contact-meches', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (invoiceType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoice-types/${invoiceType.id}`,
      }).then(() => {
        invoiceType = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('Invoices menu should load Invoices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Invoice').should('exist');
    cy.location('pathname').should('eq', invoicePageUrl);
  });

  describe('Invoice page', () => {
    it('should have translated page title', () => {
      cy.visit(invoicePageUrl);
      cy.getEntityHeading('Invoice').should('not.contain', 'snpApp.invoice.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoicePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Invoice page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${invoicePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoicePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoices',
          body: {
            ...invoiceSample,
            invoiceType,
            fromParty: party,
            toParty: party,
          },
        }).then(({ body }) => {
          invoice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/invoices?page=0&size=20>; rel="last",<http://localhost/api/invoices?page=0&size=20>; rel="first"',
              },
              body: [invoice],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoicePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Invoice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoice');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoicePageUrl);
      });

      it('edit button click should load edit Invoice page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoicePageUrl);
      });

      it('edit button click should load edit Invoice page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoicePageUrl);
      });

      it('last delete button click should delete instance of Invoice', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', invoicePageUrl);

        invoice = undefined;
      });
    });
  });

  describe('new Invoice page', () => {
    beforeEach(() => {
      cy.visit(invoicePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Invoice');
    });

    it('should create an instance of Invoice', () => {
      cy.get(`[data-cy="invoiceDate"]`).type('2023-12-14T00:06');
      cy.get(`[data-cy="invoiceDate"]`).blur();
      cy.get(`[data-cy="invoiceDate"]`).should('have.value', '2023-12-14T00:06');

      cy.get(`[data-cy="dueDate"]`).type('2023-12-14T15:41');
      cy.get(`[data-cy="dueDate"]`).blur();
      cy.get(`[data-cy="dueDate"]`).should('have.value', '2023-12-14T15:41');

      cy.get(`[data-cy="paidDate"]`).type('2023-12-14T16:28');
      cy.get(`[data-cy="paidDate"]`).blur();
      cy.get(`[data-cy="paidDate"]`).should('have.value', '2023-12-14T16:28');

      cy.get(`[data-cy="referenceNumber"]`).type('difficult archaeology');
      cy.get(`[data-cy="referenceNumber"]`).should('have.value', 'difficult archaeology');

      cy.get(`[data-cy="invoiceMessage"]`).type('astride outside hence');
      cy.get(`[data-cy="invoiceMessage"]`).should('have.value', 'astride outside hence');

      cy.get(`[data-cy="externalId"]`).type('meanwhile');
      cy.get(`[data-cy="externalId"]`).should('have.value', 'meanwhile');

      cy.get(`[data-cy="entryDate"]`).type('2023-12-14T03:13');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2023-12-14T03:13');

      cy.get(`[data-cy="grandTotal"]`).type('26774.57');
      cy.get(`[data-cy="grandTotal"]`).should('have.value', '26774.57');

      cy.get(`[data-cy="projectName"]`).type('so team where');
      cy.get(`[data-cy="projectName"]`).should('have.value', 'so team where');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="remarks"]`).type('arrogantly');
      cy.get(`[data-cy="remarks"]`).should('have.value', 'arrogantly');

      cy.get(`[data-cy="paymentTerms"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="paymentTerms"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="deliveryTerms"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="deliveryTerms"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="info"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="info"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="invoiceType"]`).select(1);
      cy.get(`[data-cy="fromParty"]`).select(1);
      cy.get(`[data-cy="toParty"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoice = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', invoicePageUrl);
    });
  });
});
