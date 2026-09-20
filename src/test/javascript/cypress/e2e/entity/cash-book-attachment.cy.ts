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

describe('CashBookAttachment e2e test', () => {
  const cashBookAttachmentPageUrl = '/cash-book-attachment';
  let username: string;
  let password: string;
  // const cashBookAttachmentSample = {};

  let cashBookAttachment;
  // let cashBook;
  // let attachment;

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
      url: '/api/cash-books',
      body: {"transactionDate":"2023-12-14","dueDate":"2023-12-14","amount":16136.58,"description":"geez","vendorCustomerName":"live fisherman","invoiceNo":"kiss ouch","invoiceDate":"2023-12-13","purpose":"furthermore tooth","remarks":"phooey limply busily","project":"uh-huh indeed","usageUnits":"steep uh-huh","recurringTransaction":true,"frequency":"QUARTERLY"},
    }).then(({ body }) => {
      cashBook = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"less unlike","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"tousle","mimeType":"mmm invite with","fileSize":19284,"uploadedDate":"2023-12-14T05:12:46.344Z","isTrashed":false,"checksum":"and"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cash-book-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cash-book-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cash-book-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cash-books', {
      statusCode: 200,
      body: [cashBook],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (cashBookAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cash-book-attachments/${cashBookAttachment.id}`,
      }).then(() => {
        cashBookAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (cashBook) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cash-books/${cashBook.id}`,
      }).then(() => {
        cashBook = undefined;
      });
    }
    if (attachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/attachments/${attachment.id}`,
      }).then(() => {
        attachment = undefined;
      });
    }
  });
   */

  it('CashBookAttachments menu should load CashBookAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cash-book-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CashBookAttachment').should('exist');
    cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
  });

  describe('CashBookAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(cashBookAttachmentPageUrl);
      cy.getEntityHeading('CashBookAttachment').should('not.contain', 'snpApp.cashBookAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cashBookAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CashBookAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cashBookAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CashBookAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cash-book-attachments',
          body: {
            ...cashBookAttachmentSample,
            cashBook: cashBook,
            attachment: attachment,
          },
        }).then(({ body }) => {
          cashBookAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cash-book-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cash-book-attachments?page=0&size=20>; rel="last",<http://localhost/api/cash-book-attachments?page=0&size=20>; rel="first"',
              },
              body: [cashBookAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cashBookAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cashBookAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CashBookAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cashBookAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
      });

      it('edit button click should load edit CashBookAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CashBookAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
      });

      it('edit button click should load edit CashBookAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CashBookAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CashBookAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cashBookAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cashBookAttachmentPageUrl);

        cashBookAttachment = undefined;
      });
    });
  });

  describe('new CashBookAttachment page', () => {
    beforeEach(() => {
      cy.visit(cashBookAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CashBookAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CashBookAttachment', () => {
      cy.get(`[data-cy="name"]`).type('ameliorate');
      cy.get(`[data-cy="name"]`).should('have.value', 'ameliorate');

      cy.get(`[data-cy="cashBook"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cashBookAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cashBookAttachmentPageUrl);
    });
  });
});
