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

describe('ShipmentAttachment e2e test', () => {
  const shipmentAttachmentPageUrl = '/shipment-attachment';
  let username: string;
  let password: string;
  const shipmentAttachmentSample = {};

  let shipmentAttachment;
  let shipment;
  let attachment;

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
      url: '/api/shipments',
      body: {
        estimatedShipDate: '2023-12-14T09:58:08.844Z',
        estimatedReadyDate: '2023-12-14T07:53:22.102Z',
        estimatedArrivalDate: '2023-12-14T06:03:32.668Z',
        estimatedShipCost: 6043.11,
      },
    }).then(({ body }) => {
      shipment = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {
        name: 'before',
        fileAttachment: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        fileAttachmentContentType: 'unknown',
        attachmentUrl: 'what',
        mimeType: 'while gah',
        fileSize: 14366,
        uploadedDate: '2023-12-14T02:39:17.156Z',
        isTrashed: true,
        checksum: 'concerning',
      },
    }).then(({ body }) => {
      attachment = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shipment-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shipment-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shipment-attachments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/shipments', {
      statusCode: 200,
      body: [shipment],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });
  });

  afterEach(() => {
    if (shipmentAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipment-attachments/${shipmentAttachment.id}`,
      }).then(() => {
        shipmentAttachment = undefined;
      });
    }
  });

  afterEach(() => {
    if (shipment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shipments/${shipment.id}`,
      }).then(() => {
        shipment = undefined;
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

  it('ShipmentAttachments menu should load ShipmentAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shipment-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ShipmentAttachment').should('exist');
    cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
  });

  describe('ShipmentAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(shipmentAttachmentPageUrl);
      cy.getEntityHeading('ShipmentAttachment').should('not.contain', 'snpApp.shipmentAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shipmentAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ShipmentAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${shipmentAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ShipmentAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shipment-attachments',
          body: {
            ...shipmentAttachmentSample,
            shipment,
            attachment,
          },
        }).then(({ body }) => {
          shipmentAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shipment-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shipment-attachments?page=0&size=20>; rel="last",<http://localhost/api/shipment-attachments?page=0&size=20>; rel="first"',
              },
              body: [shipmentAttachment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(shipmentAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ShipmentAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shipmentAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
      });

      it('edit button click should load edit ShipmentAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
      });

      it('edit button click should load edit ShipmentAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ShipmentAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
      });

      it('last delete button click should delete instance of ShipmentAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('shipmentAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', shipmentAttachmentPageUrl);

        shipmentAttachment = undefined;
      });
    });
  });

  describe('new ShipmentAttachment page', () => {
    beforeEach(() => {
      cy.visit(shipmentAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ShipmentAttachment');
    });

    it('should create an instance of ShipmentAttachment', () => {
      cy.get(`[data-cy="name"]`).type('swim concerning');
      cy.get(`[data-cy="name"]`).should('have.value', 'swim concerning');

      cy.get(`[data-cy="shipment"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        shipmentAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', shipmentAttachmentPageUrl);
    });
  });
});
