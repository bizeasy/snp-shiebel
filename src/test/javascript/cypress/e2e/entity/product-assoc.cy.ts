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

describe('ProductAssoc e2e test', () => {
  const productAssocPageUrl = '/product-assoc';
  let username: string;
  let password: string;
  const productAssocSample = { fromDate: '2023-12-14T04:50:09.619Z' };

  let productAssoc;
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
        name: 'hospitalization yahoo',
        internalName: 'orient',
        brandName: 'duh zen',
        variant: 'arbitrate',
        sku: 'whoa gray',
        introductionDate: '2023-12-14T13:08:55.703Z',
        releaseDate: '2023-12-14T17:32:00.425Z',
        quantityIncluded: 20520.41,
        piecesIncluded: 16964,
        weight: 23580.89,
        height: 3601.46,
        width: 28172.9,
        depth: 18605.16,
        smallImageUrl: 'uncover er culminate',
        mediumImageUrl: 'anenst euphonium why',
        largeImageUrl: 'indeed ravioli',
        detailImageUrl: 'lifestyle versus',
        originalImageUrl: 'duh round',
        altImageUrl1: 'blossom slime bowed',
        altImageUrl2: 'sick wherever massage',
        altImageUrl3: 'incidentally sore regularly',
        altImageUrl4: 'space',
        altImageUrl5: 'dusk',
        altImageUrl6: 'spellcheck aha',
        videoUrl: 'excluding',
        quantity: 7459.14,
        price: 10093.52,
        cgst: 15763.29,
        igst: 14255.17,
        sgst: 20274.98,
        cgstPercentage: 16270.08,
        igstPercentage: 14269.27,
        sgstPercentage: 11704.37,
        totalPrice: 30578.36,
        description: 'petty',
        longDescription: 'rapid underneath',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: true,
        requireInventory: false,
        returnable: false,
        isPopular: true,
        isBanner: false,
        bannerUrl: 'above orchid',
      },
    }).then(({ body }) => {
      product = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-assocs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-assocs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-assocs/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/product-assoc-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productAssoc) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-assocs/${productAssoc.id}`,
      }).then(() => {
        productAssoc = undefined;
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

  it('ProductAssocs menu should load ProductAssocs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-assoc');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductAssoc').should('exist');
    cy.location('pathname').should('eq', productAssocPageUrl);
  });

  describe('ProductAssoc page', () => {
    it('should have translated page title', () => {
      cy.visit(productAssocPageUrl);
      cy.getEntityHeading('ProductAssoc').should('not.contain', 'snpApp.productAssoc.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productAssocPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductAssoc page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productAssocPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductAssoc');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-assocs',
          body: {
            ...productAssocSample,
            product,
            productTo: product,
          },
        }).then(({ body }) => {
          productAssoc = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-assocs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-assocs?page=0&size=20>; rel="last",<http://localhost/api/product-assocs?page=0&size=20>; rel="first"',
              },
              body: [productAssoc],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productAssocPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductAssoc page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productAssoc');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocPageUrl);
      });

      it('edit button click should load edit ProductAssoc page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAssoc');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocPageUrl);
      });

      it('edit button click should load edit ProductAssoc page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAssoc');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocPageUrl);
      });

      it('last delete button click should delete instance of ProductAssoc', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productAssoc').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productAssocPageUrl);

        productAssoc = undefined;
      });
    });
  });

  describe('new ProductAssoc page', () => {
    beforeEach(() => {
      cy.visit(productAssocPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductAssoc');
    });

    it('should create an instance of ProductAssoc', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T19:26');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T19:26');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T05:56');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T05:56');

      cy.get(`[data-cy="sequenceNo"]`).type('2217');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '2217');

      cy.get(`[data-cy="quantity"]`).type('31305.18');
      cy.get(`[data-cy="quantity"]`).should('have.value', '31305.18');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="productTo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productAssoc = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productAssocPageUrl);
    });
  });
});
