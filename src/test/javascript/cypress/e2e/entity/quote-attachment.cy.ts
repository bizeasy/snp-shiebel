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

describe('QuoteAttachment e2e test', () => {
  const quoteAttachmentPageUrl = '/quote-attachment';
  let username: string;
  let password: string;
  // const quoteAttachmentSample = {};

  let quoteAttachment;
  // let quote;
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
      url: '/api/quotes',
      body: {"name":"unit","offerNo":"woot drat submitter","issueDate":"2023-12-14T18:15:21.892Z","validFromDate":"2023-12-14T03:40:49.144Z","validThruDate":"2023-12-14T00:36:22.049Z","description":"querulous","offerValue":6683.02,"remarks":"drat swear"},
    }).then(({ body }) => {
      quote = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"than till feather","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"nor vivacious","mimeType":"hungrily","fileSize":18796,"uploadedDate":"2023-12-14T01:58:54.009Z","isTrashed":false,"checksum":"unusual safeguard"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/quote-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quote-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quote-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/quotes', {
      statusCode: 200,
      body: [quote],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (quoteAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quote-attachments/${quoteAttachment.id}`,
      }).then(() => {
        quoteAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (quote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quotes/${quote.id}`,
      }).then(() => {
        quote = undefined;
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

  it('QuoteAttachments menu should load QuoteAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuoteAttachment').should('exist');
    cy.location('pathname').should('eq', quoteAttachmentPageUrl);
  });

  describe('QuoteAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(quoteAttachmentPageUrl);
      cy.getEntityHeading('QuoteAttachment').should('not.contain', 'snpApp.quoteAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quoteAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuoteAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quoteAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('QuoteAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quote-attachments',
          body: {
            ...quoteAttachmentSample,
            quote: quote,
            attachment: attachment,
          },
        }).then(({ body }) => {
          quoteAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quote-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quote-attachments?page=0&size=20>; rel="last",<http://localhost/api/quote-attachments?page=0&size=20>; rel="first"',
              },
              body: [quoteAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(quoteAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(quoteAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details QuoteAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quoteAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAttachmentPageUrl);
      });

      it('edit button click should load edit QuoteAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAttachmentPageUrl);
      });

      it('edit button click should load edit QuoteAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of QuoteAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quoteAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteAttachmentPageUrl);

        quoteAttachment = undefined;
      });
    });
  });

  describe('new QuoteAttachment page', () => {
    beforeEach(() => {
      cy.visit(quoteAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuoteAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of QuoteAttachment', () => {
      cy.get(`[data-cy="name"]`).type('favorite enhance');
      cy.get(`[data-cy="name"]`).should('have.value', 'favorite enhance');

      cy.get(`[data-cy="quote"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quoteAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quoteAttachmentPageUrl);
    });
  });
});
