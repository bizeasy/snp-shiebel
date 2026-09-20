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

describe('ProductFacilityLocation e2e test', () => {
  const productFacilityLocationPageUrl = '/product-facility-location';
  let username: string;
  let password: string;
  // const productFacilityLocationSample = {};

  let productFacilityLocation;
  // let product;
  // let facility;
  // let facilityLocation;

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
      url: '/api/products',
      body: {"name":"unfinished how","internalName":"insert brr while","brandName":"on","variant":"reassuringly flint because","sku":"quaintly","introductionDate":"2023-12-14T01:20:29.552Z","releaseDate":"2023-12-14T07:56:10.988Z","quantityIncluded":15007.35,"piecesIncluded":11627,"weight":19106.53,"height":1617.59,"width":12972.97,"depth":16503.25,"smallImageUrl":"um disposer","mediumImageUrl":"viciously astonishing","largeImageUrl":"convalesce lively accept","detailImageUrl":"boastfully","originalImageUrl":"injunction tightly ack","altImageUrl1":"astride because","altImageUrl2":"discrete against emphasize","altImageUrl3":"idolized consequently","altImageUrl4":"which whoever","altImageUrl5":"until pfft","altImageUrl6":"oh nectarine gallery","videoUrl":"subsidy vice furthermore","quantity":10220.28,"price":21589.58,"cgst":9108.32,"igst":18887.45,"sgst":19594.11,"cgstPercentage":19243.82,"igstPercentage":22721.84,"sgstPercentage":26359.26,"totalPrice":20847.47,"description":"even cod","longDescription":"acknowledge similar","info":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isVirtual":false,"isVariant":true,"requireInventory":true,"returnable":true,"isPopular":true,"isBanner":false,"bannerUrl":"crooked"},
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/facilities',
      body: {"name":"near internalise","description":"apud","facilitySize":20066.78},
    }).then(({ body }) => {
      facility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/facility-locations',
      body: {"locationSeqId":"loosely advertisemen","areaId":"crooked whereas inno","aisleId":"materialise joyously","sectionId":"abaft kindly christe","levelId":"approach certificati","positionId":"agile mechanically"},
    }).then(({ body }) => {
      facilityLocation = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/product-facility-locations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-facility-locations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-facility-locations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
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

    cy.intercept('GET', '/api/facility-locations', {
      statusCode: 200,
      body: [facilityLocation],
    });

  });
   */

  afterEach(() => {
    if (productFacilityLocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-facility-locations/${productFacilityLocation.id}`,
      }).then(() => {
        productFacilityLocation = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
    if (facilityLocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-locations/${facilityLocation.id}`,
      }).then(() => {
        facilityLocation = undefined;
      });
    }
  });
   */

  it('ProductFacilityLocations menu should load ProductFacilityLocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-facility-location');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFacilityLocation').should('exist');
    cy.location('pathname').should('eq', productFacilityLocationPageUrl);
  });

  describe('ProductFacilityLocation page', () => {
    it('should have translated page title', () => {
      cy.visit(productFacilityLocationPageUrl);
      cy.getEntityHeading('ProductFacilityLocation').should('not.contain', 'snpApp.productFacilityLocation.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFacilityLocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFacilityLocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFacilityLocationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFacilityLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityLocationPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-facility-locations',
          body: {
            ...productFacilityLocationSample,
            product: product,
            facility: facility,
            facilityLocation: facilityLocation,
          },
        }).then(({ body }) => {
          productFacilityLocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-facility-locations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-facility-locations?page=0&size=20>; rel="last",<http://localhost/api/product-facility-locations?page=0&size=20>; rel="first"',
              },
              body: [productFacilityLocation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFacilityLocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(productFacilityLocationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ProductFacilityLocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFacilityLocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityLocationPageUrl);
      });

      it('edit button click should load edit ProductFacilityLocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacilityLocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityLocationPageUrl);
      });

      it('edit button click should load edit ProductFacilityLocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacilityLocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityLocationPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ProductFacilityLocation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFacilityLocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityLocationPageUrl);

        productFacilityLocation = undefined;
      });
    });
  });

  describe('new ProductFacilityLocation page', () => {
    beforeEach(() => {
      cy.visit(productFacilityLocationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFacilityLocation');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ProductFacilityLocation', () => {
      cy.get(`[data-cy="minimumStock"]`).type('18393.29');
      cy.get(`[data-cy="minimumStock"]`).should('have.value', '18393.29');

      cy.get(`[data-cy="moveQuantity"]`).type('20746.78');
      cy.get(`[data-cy="moveQuantity"]`).should('have.value', '20746.78');

      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="facility"]`).select(1);
      cy.get(`[data-cy="facilityLocation"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFacilityLocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFacilityLocationPageUrl);
    });
  });
});
