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

describe('CustRequestItemAttachment e2e test', () => {
  const custRequestItemAttachmentPageUrl = '/cust-request-item-attachment';
  let username: string;
  let password: string;
  // const custRequestItemAttachmentSample = {};

  let custRequestItemAttachment;
  // let custRequestItem;
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
      url: '/api/cust-request-items',
      body: {"sequenceNo":18723,"priority":20,"requiredByDate":"2023-12-14T07:07:18.275Z","quantity":2981.56,"selectedAmount":31285.24,"maxAmount":12647.33,"description":"decouple forenenst","story":"pish watery"},
    }).then(({ body }) => {
      custRequestItem = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"smoke beside square","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"oddly","mimeType":"among","fileSize":28087,"uploadedDate":"2023-12-14T15:22:20.917Z","isTrashed":true,"checksum":"yuck mortally"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-item-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-item-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-item-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cust-request-items', {
      statusCode: 200,
      body: [custRequestItem],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (custRequestItemAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-item-attachments/${custRequestItemAttachment.id}`,
      }).then(() => {
        custRequestItemAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (custRequestItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-items/${custRequestItem.id}`,
      }).then(() => {
        custRequestItem = undefined;
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

  it('CustRequestItemAttachments menu should load CustRequestItemAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-item-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestItemAttachment').should('exist');
    cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
  });

  describe('CustRequestItemAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestItemAttachmentPageUrl);
      cy.getEntityHeading('CustRequestItemAttachment').should('not.contain', 'snpApp.custRequestItemAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestItemAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestItemAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestItemAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestItemAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-item-attachments',
          body: {
            ...custRequestItemAttachmentSample,
            custRequestItem: custRequestItem,
            attachment: attachment,
          },
        }).then(({ body }) => {
          custRequestItemAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-item-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cust-request-item-attachments?page=0&size=20>; rel="last",<http://localhost/api/cust-request-item-attachments?page=0&size=20>; rel="first"',
              },
              body: [custRequestItemAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestItemAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(custRequestItemAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CustRequestItemAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestItemAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
      });

      it('edit button click should load edit CustRequestItemAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestItemAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
      });

      it('edit button click should load edit CustRequestItemAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestItemAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CustRequestItemAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestItemAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);

        custRequestItemAttachment = undefined;
      });
    });
  });

  describe('new CustRequestItemAttachment page', () => {
    beforeEach(() => {
      cy.visit(custRequestItemAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestItemAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CustRequestItemAttachment', () => {
      cy.get(`[data-cy="name"]`).type('dramatic');
      cy.get(`[data-cy="name"]`).should('have.value', 'dramatic');

      cy.get(`[data-cy="custRequestItem"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestItemAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestItemAttachmentPageUrl);
    });
  });
});
