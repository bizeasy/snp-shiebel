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

describe('ProductFacility e2e test', () => {
  const productFacilityPageUrl = '/product-facility';
  let username: string;
  let password: string;
  const productFacilitySample = {};

  let productFacility;
  let product;
  let facility;

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
        name: 'before',
        internalName: 'account innovation mmm',
        brandName: 'vein after',
        variant: 'esteemed',
        sku: 'colorfully woefully sheepishly',
        introductionDate: '2023-12-14T01:11:38.785Z',
        releaseDate: '2023-12-14T17:54:48.772Z',
        quantityIncluded: 32754.42,
        piecesIncluded: 17585,
        weight: 3721.41,
        height: 14331.49,
        width: 28244.94,
        depth: 31824.06,
        smallImageUrl: 'useless',
        mediumImageUrl: 'solemnly till whenever',
        largeImageUrl: 'toward why ick',
        detailImageUrl: 'low beyond',
        originalImageUrl: 'carboxyl',
        altImageUrl1: 'provided though lest',
        altImageUrl2: 'like',
        altImageUrl3: 'rewarding absent',
        altImageUrl4: 'gosh',
        altImageUrl5: 'monthly',
        altImageUrl6: 'besides even amid',
        videoUrl: 'instead including',
        quantity: 3854.78,
        price: 18446.61,
        cgst: 27373.38,
        igst: 14685.61,
        sgst: 18076.36,
        cgstPercentage: 23965.72,
        igstPercentage: 4250.49,
        sgstPercentage: 13145.76,
        totalPrice: 13578.96,
        description: 'ouch blink indeed',
        longDescription: 'unwritten sinful hose',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: false,
        isVariant: true,
        requireInventory: false,
        returnable: true,
        isPopular: false,
        isBanner: true,
        bannerUrl: 'great how butter',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/facilities',
      body: { name: 'boastfully battle whether', description: 'but psst', facilitySize: 13062.89 },
    }).then(({ body }) => {
      facility = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-facilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-facilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-facilities/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/facilities', {
      statusCode: 200,
      body: [facility],
    });
  });

  afterEach(() => {
    if (productFacility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-facilities/${productFacility.id}`,
      }).then(() => {
        productFacility = undefined;
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
    if (facility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facilities/${facility.id}`,
      }).then(() => {
        facility = undefined;
      });
    }
  });

  it('ProductFacilities menu should load ProductFacilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-facility');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFacility').should('exist');
    cy.location('pathname').should('eq', productFacilityPageUrl);
  });

  describe('ProductFacility page', () => {
    it('should have translated page title', () => {
      cy.visit(productFacilityPageUrl);
      cy.getEntityHeading('ProductFacility').should('not.contain', 'snpApp.productFacility.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFacilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFacility page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFacilityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFacility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-facilities',
          body: {
            ...productFacilitySample,
            product,
            facility,
          },
        }).then(({ body }) => {
          productFacility = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-facilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-facilities?page=0&size=20>; rel="last",<http://localhost/api/product-facilities?page=0&size=20>; rel="first"',
              },
              body: [productFacility],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFacilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductFacility page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFacility');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityPageUrl);
      });

      it('edit button click should load edit ProductFacility page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityPageUrl);
      });

      it('edit button click should load edit ProductFacility page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacility');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityPageUrl);
      });

      it('last delete button click should delete instance of ProductFacility', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFacility').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityPageUrl);

        productFacility = undefined;
      });
    });
  });

  describe('new ProductFacility page', () => {
    beforeEach(() => {
      cy.visit(productFacilityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFacility');
    });

    it('should create an instance of ProductFacility', () => {
      cy.get(`[data-cy="minimumStock"]`).type('15279.76');
      cy.get(`[data-cy="minimumStock"]`).should('have.value', '15279.76');

      cy.get(`[data-cy="reorderQty"]`).type('23856.21');
      cy.get(`[data-cy="reorderQty"]`).should('have.value', '23856.21');

      cy.get(`[data-cy="daysToShip"]`).type('24982');
      cy.get(`[data-cy="daysToShip"]`).should('have.value', '24982');

      cy.get(`[data-cy="lastInventoryCount"]`).type('23479.28');
      cy.get(`[data-cy="lastInventoryCount"]`).should('have.value', '23479.28');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="facility"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFacility = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFacilityPageUrl);
    });
  });
});
