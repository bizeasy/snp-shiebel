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

describe('ProductAttachment e2e test', () => {
  const productAttachmentPageUrl = '/product-attachment';
  let username: string;
  let password: string;
  const productAttachmentSample = {};

  let productAttachment;
  let product;
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
      url: '/api/products',
      body: {
        name: 'charming yum shoulder',
        internalName: 'drain from',
        brandName: 'evil',
        variant: 'peaceful',
        sku: 'lecture',
        introductionDate: '2023-12-14T00:17:13.690Z',
        releaseDate: '2023-12-14T20:49:49.861Z',
        quantityIncluded: 30689.75,
        piecesIncluded: 7655,
        weight: 6260.61,
        height: 19302.28,
        width: 32106.18,
        depth: 32281.08,
        smallImageUrl: 'boo',
        mediumImageUrl: 'even rectangular',
        largeImageUrl: 'fortunately ew lest',
        detailImageUrl: 'exploration at',
        originalImageUrl: 'till characterization cross',
        altImageUrl1: 'hence',
        altImageUrl2: 'yet certainly',
        altImageUrl3: 'that',
        altImageUrl4: 'favorable unto',
        altImageUrl5: 'boohoo voluntarily',
        altImageUrl6: 'ack handy jury',
        videoUrl: 'because strictly broadly',
        quantity: 27099.04,
        price: 20025.67,
        cgst: 29644.99,
        igst: 6444.29,
        sgst: 19149.46,
        cgstPercentage: 30890.1,
        igstPercentage: 25390.14,
        sgstPercentage: 31651.56,
        totalPrice: 20035.67,
        description: 'menacing zowie',
        longDescription: 'quarrel',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: true,
        isVariant: true,
        requireInventory: false,
        returnable: true,
        isPopular: true,
        isBanner: false,
        bannerUrl: 'zealous reorganisation cautiously',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/attachments',
      body: {
        name: 'whoa on',
        fileAttachment: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
        fileAttachmentContentType: 'unknown',
        attachmentUrl: 'nor customise provided',
        mimeType: 'how snoopy muddy',
        fileSize: 14823,
        uploadedDate: '2023-12-14T13:58:53.416Z',
        isTrashed: false,
        checksum: 'under scale',
      },
    }).then(({ body }) => {
      attachment = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-attachments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-attachments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-attachments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/attachments', {
      statusCode: 200,
      body: [attachment],
    });
  });

  afterEach(() => {
    if (productAttachment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-attachments/${productAttachment.id}`,
      }).then(() => {
        productAttachment = undefined;
      });
    }
  });

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
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

  it('ProductAttachments menu should load ProductAttachments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-attachment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductAttachment').should('exist');
    cy.location('pathname').should('eq', productAttachmentPageUrl);
  });

  describe('ProductAttachment page', () => {
    it('should have translated page title', () => {
      cy.visit(productAttachmentPageUrl);
      cy.getEntityHeading('ProductAttachment').should('not.contain', 'snpApp.productAttachment.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productAttachmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductAttachment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productAttachmentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAttachmentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-attachments',
          body: {
            ...productAttachmentSample,
            product,
            attachment,
          },
        }).then(({ body }) => {
          productAttachment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-attachments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-attachments?page=0&size=20>; rel="last",<http://localhost/api/product-attachments?page=0&size=20>; rel="first"',
              },
              body: [productAttachment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productAttachmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductAttachment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productAttachment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAttachmentPageUrl);
      });

      it('edit button click should load edit ProductAttachment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAttachment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAttachmentPageUrl);
      });

      it('edit button click should load edit ProductAttachment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAttachment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAttachmentPageUrl);
      });

      it('last delete button click should delete instance of ProductAttachment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productAttachment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAttachmentPageUrl);

        productAttachment = undefined;
      });
    });
  });

  describe('new ProductAttachment page', () => {
    beforeEach(() => {
      cy.visit(productAttachmentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductAttachment');
    });

    it('should create an instance of ProductAttachment', () => {
      cy.get(`[data-cy="name"]`).type('huzzah aside oh');
      cy.get(`[data-cy="name"]`).should('have.value', 'huzzah aside oh');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="attachment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productAttachment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productAttachmentPageUrl);
    });
  });
});
