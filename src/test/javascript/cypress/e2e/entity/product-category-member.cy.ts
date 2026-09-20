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

describe('ProductCategoryMember e2e test', () => {
  const productCategoryMemberPageUrl = '/product-category-member';
  let username: string;
  let password: string;
  const productCategoryMemberSample = { fromDate: '2023-12-13T23:19:25.822Z' };

  let productCategoryMember;
  let product;
  let productCategory;

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
        name: 'presell lined boo',
        internalName: 'bah unless',
        brandName: 'that',
        variant: 'gee after',
        sku: 'boohoo as',
        introductionDate: '2023-12-13T22:58:55.395Z',
        releaseDate: '2023-12-14T03:29:40.758Z',
        quantityIncluded: 1882.14,
        piecesIncluded: 15558,
        weight: 287.07,
        height: 9811.09,
        width: 14965.74,
        depth: 28661.97,
        smallImageUrl: 'license mortar',
        mediumImageUrl: 'shoot republican drat',
        largeImageUrl: 'flowery',
        detailImageUrl: 'season garage',
        originalImageUrl: 'er geez',
        altImageUrl1: 'than',
        altImageUrl2: 'brr',
        altImageUrl3: 'nor fellow trusty',
        altImageUrl4: 'whereas',
        altImageUrl5: 'simplistic toward',
        altImageUrl6: 'mismatch quaintly',
        videoUrl: 'huzzah gah degrease',
        quantity: 25438.04,
        price: 21799.46,
        cgst: 8923.49,
        igst: 28293.2,
        sgst: 22358.15,
        cgstPercentage: 13519.17,
        igstPercentage: 4894.63,
        sgstPercentage: 28567.28,
        totalPrice: 10691.06,
        description: 'submissive blah',
        longDescription: 'pish sheepishly',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: true,
        isVariant: true,
        requireInventory: true,
        returnable: true,
        isPopular: true,
        isBanner: false,
        bannerUrl: 'uh-huh pro',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-categories',
      body: {
        name: 'firm cruelly yahoo',
        description: 'whistle',
        longDescription: 'winged internalise',
        attribute: 'sniff heighten so',
        sequenceNo: 24207,
        imageUrl: 'diligent',
        altImageUrl: 'row ravage which',
        bannerUrl: 'um',
        largeImageUrl: 'ah',
        altLargeImageUrl: 'what forgo down',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isBanner: true,
      },
    }).then(({ body }) => {
      productCategory = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-category-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-category-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-category-members/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/product-categories', {
      statusCode: 200,
      body: [productCategory],
    });
  });

  afterEach(() => {
    if (productCategoryMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-category-members/${productCategoryMember.id}`,
      }).then(() => {
        productCategoryMember = undefined;
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
    if (productCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-categories/${productCategory.id}`,
      }).then(() => {
        productCategory = undefined;
      });
    }
  });

  it('ProductCategoryMembers menu should load ProductCategoryMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-category-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductCategoryMember').should('exist');
    cy.location('pathname').should('eq', productCategoryMemberPageUrl);
  });

  describe('ProductCategoryMember page', () => {
    it('should have translated page title', () => {
      cy.visit(productCategoryMemberPageUrl);
      cy.getEntityHeading('ProductCategoryMember').should('not.contain', 'snpApp.productCategoryMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productCategoryMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductCategoryMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productCategoryMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductCategoryMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-category-members',
          body: {
            ...productCategoryMemberSample,
            product,
            productCategory,
          },
        }).then(({ body }) => {
          productCategoryMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-category-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-category-members?page=0&size=20>; rel="last",<http://localhost/api/product-category-members?page=0&size=20>; rel="first"',
              },
              body: [productCategoryMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productCategoryMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductCategoryMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productCategoryMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryMemberPageUrl);
      });

      it('edit button click should load edit ProductCategoryMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategoryMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryMemberPageUrl);
      });

      it('edit button click should load edit ProductCategoryMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductCategoryMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryMemberPageUrl);
      });

      it('last delete button click should delete instance of ProductCategoryMember', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productCategoryMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productCategoryMemberPageUrl);

        productCategoryMember = undefined;
      });
    });
  });

  describe('new ProductCategoryMember page', () => {
    beforeEach(() => {
      cy.visit(productCategoryMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductCategoryMember');
    });

    it('should create an instance of ProductCategoryMember', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T08:32');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T08:32');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T06:55');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T06:55');

      cy.get(`[data-cy="sequenceNo"]`).type('20425');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '20425');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="productCategory"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productCategoryMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productCategoryMemberPageUrl);
    });
  });
});
