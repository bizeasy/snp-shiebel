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

describe('ProductPrice e2e test', () => {
  const productPricePageUrl = '/product-price';
  let username: string;
  let password: string;
  const productPriceSample = { fromDate: '2023-12-14T19:15:24.168Z', price: 23274.38 };

  let productPrice;
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
        name: 'chase',
        internalName: 'after',
        brandName: 'duster bend',
        variant: 'circa outsource drat',
        sku: 'merrily',
        introductionDate: '2023-12-14T21:07:21.502Z',
        releaseDate: '2023-12-14T03:32:21.542Z',
        quantityIncluded: 4386.66,
        piecesIncluded: 16006,
        weight: 4921.41,
        height: 1368.31,
        width: 13043.27,
        depth: 12249.76,
        smallImageUrl: 'indeed giving crossly',
        mediumImageUrl: 'orientate so mechanically',
        largeImageUrl: 'babushka',
        detailImageUrl: 'uncork',
        originalImageUrl: 'coarse',
        altImageUrl1: 'thump',
        altImageUrl2: 'since but via',
        altImageUrl3: 'upwardly',
        altImageUrl4: 'eek redact emphasise',
        altImageUrl5: 'whoa black',
        altImageUrl6: 'ick worth',
        videoUrl: 'even knowingly',
        quantity: 14570.76,
        price: 17083.08,
        cgst: 20656.3,
        igst: 17541.23,
        sgst: 15809.46,
        cgstPercentage: 25969.94,
        igstPercentage: 20290.5,
        sgstPercentage: 13863.44,
        totalPrice: 2510.98,
        description: 'aside meanwhile',
        longDescription: 'corporation',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: false,
        requireInventory: true,
        returnable: false,
        isPopular: true,
        isBanner: true,
        bannerUrl: 'over',
      },
    }).then(({ body }) => {
      product = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-prices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-prices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-prices/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/product-price-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-price-purposes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productPrice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-prices/${productPrice.id}`,
      }).then(() => {
        productPrice = undefined;
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

  it('ProductPrices menu should load ProductPrices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-price');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductPrice').should('exist');
    cy.location('pathname').should('eq', productPricePageUrl);
  });

  describe('ProductPrice page', () => {
    it('should have translated page title', () => {
      cy.visit(productPricePageUrl);
      cy.getEntityHeading('ProductPrice').should('not.contain', 'snpApp.productPrice.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPricePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductPrice page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productPricePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductPrice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-prices',
          body: {
            ...productPriceSample,
            product,
          },
        }).then(({ body }) => {
          productPrice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-prices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-prices?page=0&size=20>; rel="last",<http://localhost/api/product-prices?page=0&size=20>; rel="first"',
              },
              body: [productPrice],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPricePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductPrice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productPrice');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePageUrl);
      });

      it('edit button click should load edit ProductPrice page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPrice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePageUrl);
      });

      it('edit button click should load edit ProductPrice page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductPrice');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePageUrl);
      });

      it('last delete button click should delete instance of ProductPrice', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productPrice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPricePageUrl);

        productPrice = undefined;
      });
    });
  });

  describe('new ProductPrice page', () => {
    beforeEach(() => {
      cy.visit(productPricePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductPrice');
    });

    it('should create an instance of ProductPrice', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T14:44');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T14:44');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T03:05');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T03:05');

      cy.get(`[data-cy="price"]`).type('2707.12');
      cy.get(`[data-cy="price"]`).should('have.value', '2707.12');

      cy.get(`[data-cy="cgst"]`).type('28621.55');
      cy.get(`[data-cy="cgst"]`).should('have.value', '28621.55');

      cy.get(`[data-cy="igst"]`).type('19139.64');
      cy.get(`[data-cy="igst"]`).should('have.value', '19139.64');

      cy.get(`[data-cy="sgst"]`).type('28224.86');
      cy.get(`[data-cy="sgst"]`).should('have.value', '28224.86');

      cy.get(`[data-cy="totalPrice"]`).type('25415.77');
      cy.get(`[data-cy="totalPrice"]`).should('have.value', '25415.77');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productPrice = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productPricePageUrl);
    });
  });
});
