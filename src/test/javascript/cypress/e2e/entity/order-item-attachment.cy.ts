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

describe('OrderItemAttachment e2e test', () => {
  const orderItemAttachmentPageUrl = '/order-item-attachment';
  let username: string;
  let password: string;
  // const orderItemAttachmentSample = {};

  let orderItemAttachment;
  // let orderItem;
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
      url: '/api/order-items',
      body: {"sequenceNo":8755,"quantity":4300.34,"cancelQuantity":7695.49,"selectedAmount":22007.45,"unitPrice":14562.17,"unitListPrice":31120.04,"isModifiedPrice":true,"estimatedShipDate":"2023-12-14T00:08:36.676Z","estimatedDeliveryDate":"2023-12-14T18:50:25.972Z","shipBeforeDate":"2023-12-14T10:26:57.409Z","shipAfterDate":"2023-12-14T08:05:50.422Z","entryDate":"2023-12-14T01:37:32.733Z","attr1":"window phooey despite","attr2":"innocently unless generously","attr3":"meanwhile"},
    }).then(({ body }) => {
      orderItem = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {"name":"opposite even utilized","fileAttachment":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","fileAttachmentContentType":"unknown","attachmentUrl":"tangible barring","mimeType":"stabilise square whereas","fileSize":18818,"uploadedDate":"2023-12-13T23:11:20.229Z","isTrashed":false,"checksum":"gee though distant"},
    }).then(({ body }) => {
      attachment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/order-item-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-item-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-item-attachments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [orderItem],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });

  });
   */

  afterEach(() => {
    if (orderItemAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-item-attachments/${orderItemAttachment.id}`,
      }).then(() => {
        orderItemAttachment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (orderItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-items/${orderItem.id}`,
      }).then(() => {
        orderItem = undefined;
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

  it('OrderItemAttachments menu should load OrderItemAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-item-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderItemAttachment').should('exist');
    cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
  });

  describe('OrderItemAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(orderItemAttachmentPageUrl);
      cy.getEntityHeading('OrderItemAttachment').should('not.contain', 'snpApp.orderItemAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderItemAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderItemAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderItemAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderItemAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-item-attachments',
          body: {
            ...orderItemAttachmentSample,
            orderItem: orderItem,
            attachment: attachment,
          },
        }).then(({ body }) => {
          orderItemAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-item-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-item-attachments?page=0&size=20>; rel="last",<http://localhost/api/order-item-attachments?page=0&size=20>; rel="first"',
              },
              body: [orderItemAttachment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderItemAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(orderItemAttachmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details OrderItemAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderItemAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
      });

      it('edit button click should load edit OrderItemAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
      });

      it('edit button click should load edit OrderItemAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderItemAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of OrderItemAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderItemAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderItemAttachmentPageUrl);

        orderItemAttachment = undefined;
      });
    });
  });

  describe('new OrderItemAttachment page', () => {
    beforeEach(() => {
      cy.visit(orderItemAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderItemAttachment');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of OrderItemAttachment', () => {
      cy.get(`[data-cy="name"]`).type('quash');
      cy.get(`[data-cy="name"]`).should('have.value', 'quash');

      cy.get(`[data-cy="orderItem"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderItemAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderItemAttachmentPageUrl);
    });
  });
});
