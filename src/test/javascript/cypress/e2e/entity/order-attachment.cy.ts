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

describe('OrderAttachment e2e test', () => {
  const orderAttachmentPageUrl = '/order-attachment';
  let username: string;
  let password: string;
  // const orderAttachmentSample = {};

  let orderAttachment;
  // let order;
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
      url: '/api/orders',
      body: {"name":"grandiose","orderRefNo":"vaguely finally supportive","externalId":"deliquesce instance","orderDate":"2023-12-14T16:23:51.421Z","entryDate":"2023-12-14T20:30:24.570Z","deliveryDate":"2023-12-14","priority":10,"isRushOrder":false,"needsInventoryIssuance":true,"remainingSubTotal":370.26,"grandTotal":15330.43,"projectName":"however ew spectacles","version":13214,"versionRef":"secret peony","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","remarks":"shark brr supposing","paymentTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","deliveryTerms":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","attr1":"creak gadzooks","attr2":"actual","attr3":"about bitter"},
    }).then(({ body }) => {
      order = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"into","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"big whoa","mimeType":"or qua deliberately","fileSize":20421,"uploadedDate":"2023-12-13T21:34:11.109Z","isTrashed":true,"checksum":"haul"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [order],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (orderAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-attachments/${orderAttachment.id}`,
      }).then(() => {
        orderAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (order) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/orders/${order.id}`,
      }).then(() => {
        order = undefined;
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

  it('OrderAttachments menu should load OrderAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderAttachment').should('exist');
    cy.location('pathname').should('eq', orderAttachmentPageUrl);
  });

  describe('OrderAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(orderAttachmentPageUrl);
      cy.getEntityHeading('OrderAttachment').should('not.contain', 'snpApp.orderAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-attachments',
          body: {
            ...orderAttachmentSample,
            order: order,
            attachment: attachment,
          },
        }).then(({ body }) => {
          orderAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-attachments?page=0&size=20>; rel="last",<http://localhost/api/order-attachments?page=0&size=20>; rel="first"',
              },
              body: [orderAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAttachmentPageUrl);
      });

      it('edit button click should load edit OrderAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAttachmentPageUrl);
      });

      it('edit button click should load edit OrderAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderAttachmentPageUrl);

        orderAttachment = undefined;
      });
    });
  });

  describe('new OrderAttachment page', () => {
    beforeEach(() => {
      cy.visit(orderAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderAttachment', () => {
      cy.get(`[data-cy="name"]`).type('esteemed while');
      cy.get(`[data-cy="name"]`).should('have.value', 'esteemed while');

      cy.get(`[data-cy="order"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderAttachmentPageUrl);
    });
  });
});
