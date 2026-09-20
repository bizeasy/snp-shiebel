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

describe('ProductFacilityUser e2e test', () => {
  const productFacilityUserPageUrl = '/product-facility-user';
  let username: string;
  let password: string;
  // const productFacilityUserSample = {"fromDate":"2023-12-14T03:08:56.007Z"};

  let productFacilityUser;
  // let productFacility;
  // let party;

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
      url: '/api/product-facilities',
      body: {"minimumStock":13444.25,"reorderQty":15746.39,"daysToShip":25859,"lastInventoryCount":20522.56},
    }).then(({ body }) => {
      productFacility = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {"isOrganisation":false,"organisationName":"innocently into arraign","organisationShortName":"whereas","salutation":"neck limp","firstName":"Ubaldo","middleName":"unnaturally however","lastName":"Cole","gender":"FEMALE","birthDate":"2023-12-14","primaryPhone":"inexperienced","primaryEmail":"smart what sushi","isTemporaryPassword":true,"logoImageUrl":"hence hunt","profileImageUrl":"suspiciously gadzooks","notes":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","postalAddress":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="},
    }).then(({ body }) => {
      party = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/product-facility-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-facility-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-facility-users/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/product-facilities', {
      statusCode: 200,
      body: [productFacility],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (productFacilityUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-facility-users/${productFacilityUser.id}`,
      }).then(() => {
        productFacilityUser = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (productFacility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-facilities/${productFacility.id}`,
      }).then(() => {
        productFacility = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });
   */

  it('ProductFacilityUsers menu should load ProductFacilityUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-facility-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductFacilityUser').should('exist');
    cy.location('pathname').should('eq', productFacilityUserPageUrl);
  });

  describe('ProductFacilityUser page', () => {
    it('should have translated page title', () => {
      cy.visit(productFacilityUserPageUrl);
      cy.getEntityHeading('ProductFacilityUser').should('not.contain', 'snpApp.productFacilityUser.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productFacilityUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductFacilityUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productFacilityUserPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductFacilityUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityUserPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-facility-users',
          body: {
            ...productFacilityUserSample,
            productFacility: productFacility,
            party: party,
          },
        }).then(({ body }) => {
          productFacilityUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-facility-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-facility-users?page=0&size=20>; rel="last",<http://localhost/api/product-facility-users?page=0&size=20>; rel="first"',
              },
              body: [productFacilityUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productFacilityUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(productFacilityUserPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ProductFacilityUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productFacilityUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityUserPageUrl);
      });

      it('edit button click should load edit ProductFacilityUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacilityUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityUserPageUrl);
      });

      it('edit button click should load edit ProductFacilityUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductFacilityUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityUserPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ProductFacilityUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productFacilityUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productFacilityUserPageUrl);

        productFacilityUser = undefined;
      });
    });
  });

  describe('new ProductFacilityUser page', () => {
    beforeEach(() => {
      cy.visit(productFacilityUserPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductFacilityUser');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ProductFacilityUser', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T20:54');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T20:54');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T00:46');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T00:46');

      cy.get(`[data-cy="costPerDay"]`).type('5781.06');
      cy.get(`[data-cy="costPerDay"]`).should('have.value', '5781.06');

      cy.get(`[data-cy="totalCost"]`).type('23948.09');
      cy.get(`[data-cy="totalCost"]`).should('have.value', '23948.09');

      cy.get(`[data-cy="discount"]`).type('1092.97');
      cy.get(`[data-cy="discount"]`).should('have.value', '1092.97');

      cy.get(`[data-cy="productFacility"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productFacilityUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productFacilityUserPageUrl);
    });
  });
});
