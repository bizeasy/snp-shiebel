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

describe('CustRequestAttachment e2e test', () => {
  const custRequestAttachmentPageUrl = '/cust-request-attachment';
  let username: string;
  let password: string;
  // const custRequestAttachmentSample = {};

  let custRequestAttachment;
  // let custRequest;
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
      url: '/api/cust-requests',
      body: {"name":"fill failing tremendously","description":"tuxedo ha","priority":13,"custRequestDate":"2023-12-14T00:37:08.029Z","responseRequiredDate":"2023-12-14T18:32:49.033Z","openDateTime":"2023-12-14T18:34:16.001Z","closedDateTime":"2023-12-14T17:41:20.419Z","internalComment":"toward maul","billed":true,"custEstimatedDurationDays":1173},
    }).then(({ body }) => {
      custRequest = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"yieldingly card","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"around finally","mimeType":"inside","fileSize":14648,"uploadedDate":"2023-12-14T09:30:33.537Z","isTrashed":false,"checksum":"armchair"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cust-requests', {
      statusCode: 200,
      body: [custRequest],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (custRequestAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-attachments/${custRequestAttachment.id}`,
      }).then(() => {
        custRequestAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (custRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-requests/${custRequest.id}`,
      }).then(() => {
        custRequest = undefined;
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

  it('CustRequestAttachments menu should load CustRequestAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestAttachment').should('exist');
    cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
  });

  describe('CustRequestAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestAttachmentPageUrl);
      cy.getEntityHeading('CustRequestAttachment').should('not.contain', 'snpApp.custRequestAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-attachments',
          body: {
            ...custRequestAttachmentSample,
            custRequest: custRequest,
            attachment: attachment,
          },
        }).then(({ body }) => {
          custRequestAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cust-request-attachments?page=0&size=20>; rel="last",<http://localhost/api/cust-request-attachments?page=0&size=20>; rel="first"',
              },
              body: [custRequestAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(custRequestAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CustRequestAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
      });

      it('edit button click should load edit CustRequestAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
      });

      it('edit button click should load edit CustRequestAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CustRequestAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestAttachmentPageUrl);

        custRequestAttachment = undefined;
      });
    });
  });

  describe('new CustRequestAttachment page', () => {
    beforeEach(() => {
      cy.visit(custRequestAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CustRequestAttachment', () => {
      cy.get(`[data-cy="name"]`).type('huzzah gah');
      cy.get(`[data-cy="name"]`).should('have.value', 'huzzah gah');

      cy.get(`[data-cy="custRequest"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestAttachmentPageUrl);
    });
  });
});
