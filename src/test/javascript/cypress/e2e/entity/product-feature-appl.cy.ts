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

describe('ProductFeatureAppl e2e test', () => {
  const productFeatureApplPageUrl = '/product-feature-appl';
  let username: string;
  let password: string;
  const productFeatureApplSample = { fromDate: '2023-12-14T04:39:58.967Z' };

  let productFeatureAppl;
  let product;
  let productFeature;

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
        name: 'feather rectangular excepting',
        internalName: 'afterwards beside fast',
        brandName: 'marksman',
        variant: 'lowball',
        sku: 'considering come',
        introductionDate: '2023-12-14T14:05:50.175Z',
        releaseDate: '2023-12-13T23:43:12.396Z',
        quantityIncluded: 16046.43,
        piecesIncluded: 23540,
        weight: 10312.74,
        height: 17913.91,
        width: 17950.45,
        depth: 27415.82,
        smallImageUrl: 'cemetery hm',
        mediumImageUrl: 'whose',
        largeImageUrl: 'gift till',
        detailImageUrl: 'between ack worse',
        originalImageUrl: 'despite excluding navigate',
        altImageUrl1: 'um',
        altImageUrl2: 'rudely membership',
        altImageUrl3: 'saturate',
        altImageUrl4: 'innocent bulky reluctantly',
        altImageUrl5: 'supposing loaf meatloaf',
        altImageUrl6: 'pfft urgently yawningly',
        videoUrl: 'atop',
        quantity: 7397.54,
        price: 17815.27,
        cgst: 11040.84,
        igst: 6340.59,
        sgst: 31457.17,
        cgstPercentage: 12657.92,
        igstPercentage: 1885.51,
        sgstPercentage: 29112.09,
        totalPrice: 32446.58,
        description: 'until regarding between',
        longDescription: 'oof daintily',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: false,
        requireInventory: true,
        returnable: true,
        isPopular: false,
        isBanner: true,
        bannerUrl: 'now',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-features',
      body: { name: 'miserably meanwhile', description: 'yuck', amount: 4971.62, sequenceNo: 20968, abbrev: 'cruel left when' },
    }).then(({ body }) => {
      productFeature = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-feature-appls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-feature-appls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-feature-appls/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/product-features', {
      statusCode: 200,
      body: [productFeature],
    });

    cy.intercept('GET', '/api/product-feature-appl-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productFeatureAppl) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-feature-appls/${productFeatureAppl.id}`,
      }).then(() => {
        productFeatureAppl = undefined;
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
    if (productFeature) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-features/${productFeature.id}`,
      }).then(() => {
        productFeature = undefined;
      });
    }
  });

  it('ProductFeatureAppls menu should load ProductFeatureAppls page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-feature-appl');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFeatureAppl').should('exist');
    cy.location('pathname').should('eq', productFeatureApplPageUrl);
  });

  describe('ProductFeatureAppl page', () => {
    it('should have translated page title', () => {
      cy.visit(productFeatureApplPageUrl);
      cy.getEntityHeading('ProductFeatureAppl').should('not.contain', 'snpApp.productFeatureAppl.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFeatureApplPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFeatureAppl page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFeatureApplPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFeatureAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-feature-appls',
          body: {
            ...productFeatureApplSample,
            product,
            productFeature,
          },
        }).then(({ body }) => {
          productFeatureAppl = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-feature-appls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-feature-appls?page=0&size=20>; rel="last",<http://localhost/api/product-feature-appls?page=0&size=20>; rel="first"',
              },
              body: [productFeatureAppl],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFeatureApplPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFeatureAppl page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFeatureAppl');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureAppl page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureAppl');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplPageUrl);
      });

      it('edit button click should load edit ProductFeatureAppl page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFeatureAppl');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplPageUrl);
      });

      it('last delete button click should delete instance of ProductFeatureAppl', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFeatureAppl').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFeatureApplPageUrl);

        productFeatureAppl = undefined;
      });
    });
  });

  describe('new ProductFeatureAppl page', () => {
    beforeEach(() => {
      cy.visit(productFeatureApplPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFeatureAppl');
    });

    it('should create an instance of ProductFeatureAppl', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T05:05');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T05:05');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T17:43');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T17:43');

      cy.get(`[data-cy="sequenceNo"]`).type('350');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '350');

      cy.get(`[data-cy="amount"]`).type('21519.82');
      cy.get(`[data-cy="amount"]`).should('have.value', '21519.82');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="productFeature"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFeatureAppl = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFeatureApplPageUrl);
    });
  });
});
