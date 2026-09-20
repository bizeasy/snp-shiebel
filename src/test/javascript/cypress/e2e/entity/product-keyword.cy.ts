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

describe('ProductKeyword e2e test', () => {
  const productKeywordPageUrl = '/product-keyword';
  let username: string;
  let password: string;
  const productKeywordSample = { keyword: 'to vista' };

  let productKeyword;
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
        name: 'ew meanwhile when',
        internalName: 'creamy hopelessly',
        brandName: 'since an tenement',
        variant: 'release',
        sku: 'wherever because',
        introductionDate: '2023-12-14T13:32:21.202Z',
        releaseDate: '2023-12-13T22:39:21.479Z',
        quantityIncluded: 27359.64,
        piecesIncluded: 13086,
        weight: 28866.27,
        height: 22976.26,
        width: 32660.66,
        depth: 14091.82,
        smallImageUrl: 'fat',
        mediumImageUrl: 'sympathetically next hamburger',
        largeImageUrl: 'gah muscat um',
        detailImageUrl: 'beard',
        originalImageUrl: 'gentle frozen psst',
        altImageUrl1: 'per',
        altImageUrl2: 'certainly thankfully expansion',
        altImageUrl3: 'yowza',
        altImageUrl4: 'off',
        altImageUrl5: 'whenever scare',
        altImageUrl6: 'arraign',
        videoUrl: 'provider frozen pish',
        quantity: 25985.65,
        price: 6615.64,
        cgst: 12473.92,
        igst: 29697.84,
        sgst: 12670.67,
        cgstPercentage: 15809.48,
        igstPercentage: 9673.7,
        sgstPercentage: 11181.97,
        totalPrice: 3463,
        description: 'tuba aboard consequently',
        longDescription: 'simple',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: false,
        requireInventory: true,
        returnable: false,
        isPopular: false,
        isBanner: false,
        bannerUrl: 'since',
      },
    }).then(({ body }) => {
      product = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-keywords+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-keywords').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-keywords/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/keyword-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productKeyword) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-keywords/${productKeyword.id}`,
      }).then(() => {
        productKeyword = undefined;
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

  it('ProductKeywords menu should load ProductKeywords page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-keyword');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductKeyword').should('exist');
    cy.location('pathname').should('eq', productKeywordPageUrl);
  });

  describe('ProductKeyword page', () => {
    it('should have translated page title', () => {
      cy.visit(productKeywordPageUrl);
      cy.getEntityHeading('ProductKeyword').should('not.contain', 'snpApp.productKeyword.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productKeywordPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductKeyword page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productKeywordPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductKeyword');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productKeywordPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-keywords',
          body: {
            ...productKeywordSample,
            product,
          },
        }).then(({ body }) => {
          productKeyword = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-keywords+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-keywords?page=0&size=20>; rel="last",<http://localhost/api/product-keywords?page=0&size=20>; rel="first"',
              },
              body: [productKeyword],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productKeywordPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductKeyword page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productKeyword');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productKeywordPageUrl);
      });

      it('edit button click should load edit ProductKeyword page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductKeyword');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productKeywordPageUrl);
      });

      it('edit button click should load edit ProductKeyword page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductKeyword');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productKeywordPageUrl);
      });

      it('last delete button click should delete instance of ProductKeyword', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productKeyword').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productKeywordPageUrl);

        productKeyword = undefined;
      });
    });
  });

  describe('new ProductKeyword page', () => {
    beforeEach(() => {
      cy.visit(productKeywordPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductKeyword');
    });

    it('should create an instance of ProductKeyword', () => {
      cy.get(`[data-cy="keyword"]`).type('as fairly');
      cy.get(`[data-cy="keyword"]`).should('have.value', 'as fairly');

      cy.get(`[data-cy="relevancyWeight"]`).type('24140');
      cy.get(`[data-cy="relevancyWeight"]`).should('have.value', '24140');

      cy.get(`[data-cy="product"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productKeyword = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productKeywordPageUrl);
    });
  });
});
