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

describe('ProductReview e2e test', () => {
  const productReviewPageUrl = '/product-review';
  let username: string;
  let password: string;
  const productReviewSample = {};

  let productReview;
  let product;

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
        name: 'why',
        internalName: 'house musty',
        brandName: 'next',
        variant: 'what',
        sku: 'tune',
        introductionDate: '2023-12-13T23:37:27.684Z',
        releaseDate: '2023-12-14T09:04:38.968Z',
        quantityIncluded: 18567.15,
        piecesIncluded: 29675,
        weight: 19525.51,
        height: 16055.43,
        width: 23437.56,
        depth: 14379.65,
        smallImageUrl: 'tennis rudely',
        mediumImageUrl: 'fooey enhance petty',
        largeImageUrl: 'sniveling with drat',
        detailImageUrl: 'gently skean',
        originalImageUrl: 'because',
        altImageUrl1: 'frightfully reflate',
        altImageUrl2: 'easily aboard which',
        altImageUrl3: 'hence after cap',
        altImageUrl4: 'past yahoo',
        altImageUrl5: 'habit yahoo extremely',
        altImageUrl6: 'cinch after',
        videoUrl: 'worthwhile',
        quantity: 4696.88,
        price: 28119.19,
        cgst: 7688.16,
        igst: 22115.03,
        sgst: 360.9,
        cgstPercentage: 1619.62,
        igstPercentage: 3602.93,
        sgstPercentage: 27084.27,
        totalPrice: 5962.35,
        description: 'bleak worth',
        longDescription: 'contrast',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: true,
        isVariant: false,
        requireInventory: true,
        returnable: true,
        isPopular: false,
        isBanner: false,
        bannerUrl: 'bleach once at',
      },
    }).then(({ body }) => {
      product = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-reviews+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-reviews').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-reviews/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productReview) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-reviews/${productReview.id}`,
      }).then(() => {
        productReview = undefined;
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
  });

  it('ProductReviews menu should load ProductReviews page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-review');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductReview').should('exist');
    cy.location('pathname').should('eq', productReviewPageUrl);
  });

  describe('ProductReview page', () => {
    it('should have translated page title', () => {
      cy.visit(productReviewPageUrl);
      cy.getEntityHeading('ProductReview').should('not.contain', 'snpApp.productReview.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productReviewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductReview page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productReviewPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productReviewPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-reviews',
          body: {
            ...productReviewSample,
            product,
          },
        }).then(({ body }) => {
          productReview = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-reviews+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-reviews?page=0&size=20>; rel="last",<http://localhost/api/product-reviews?page=0&size=20>; rel="first"',
              },
              body: [productReview],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productReviewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductReview page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productReview');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productReviewPageUrl);
      });

      it('edit button click should load edit ProductReview page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productReviewPageUrl);
      });

      it('edit button click should load edit ProductReview page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductReview');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productReviewPageUrl);
      });

      it('last delete button click should delete instance of ProductReview', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productReview').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productReviewPageUrl);

        productReview = undefined;
      });
    });
  });

  describe('new ProductReview page', () => {
    beforeEach(() => {
      cy.visit(productReviewPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductReview');
    });

    it('should create an instance of ProductReview', () => {
      cy.get(`[data-cy="rating"]`).type('5');
      cy.get(`[data-cy="rating"]`).should('have.value', '5');

      cy.get(`[data-cy="ratingInfo"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="ratingInfo"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="message"]`).type('for creaking');
      cy.get(`[data-cy="message"]`).should('have.value', 'for creaking');

      cy.get(`[data-cy="reviewDate"]`).type('2023-12-14T20:49');
      cy.get(`[data-cy="reviewDate"]`).blur();
      cy.get(`[data-cy="reviewDate"]`).should('have.value', '2023-12-14T20:49');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productReview = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productReviewPageUrl);
    });
  });
});
