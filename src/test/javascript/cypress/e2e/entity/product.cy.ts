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

describe('Product e2e test', () => {
  const productPageUrl = '/product';
  let username: string;
  let password: string;
  const productSample = { name: 'youthfully guilt' };

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
    cy.intercept('GET', '/api/products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/products/*').as('deleteEntityRequest');
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

  it('Products menu should load Products page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Product').should('exist');
    cy.location('pathname').should('eq', productPageUrl);
  });

  describe('Product page', () => {
    it('should have translated page title', () => {
      cy.visit(productPageUrl);
      cy.getEntityHeading('Product').should('not.contain', 'snpApp.product.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Product page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/products',
          body: productSample,
        }).then(({ body }) => {
          product = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/products?page=0&size=20>; rel="last",<http://localhost/api/products?page=0&size=20>; rel="first"',
              },
              body: [product],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Product page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('product');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPageUrl);
      });

      it('edit button click should load edit Product page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPageUrl);
      });

      it('edit button click should load edit Product page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Product');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPageUrl);
      });

      it('last delete button click should delete instance of Product', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('product').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productPageUrl);

        product = undefined;
      });
    });
  });

  describe('new Product page', () => {
    beforeEach(() => {
      cy.visit(productPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Product');
    });

    it('should create an instance of Product', () => {
      cy.get(`[data-cy="name"]`).type('sans');
      cy.get(`[data-cy="name"]`).should('have.value', 'sans');

      cy.get(`[data-cy="internalName"]`).type('plastic dazzling');
      cy.get(`[data-cy="internalName"]`).should('have.value', 'plastic dazzling');

      cy.get(`[data-cy="brandName"]`).type('slake gee though');
      cy.get(`[data-cy="brandName"]`).should('have.value', 'slake gee though');

      cy.get(`[data-cy="variant"]`).type('yowza');
      cy.get(`[data-cy="variant"]`).should('have.value', 'yowza');

      cy.get(`[data-cy="sku"]`).type('reporter well-worn hence');
      cy.get(`[data-cy="sku"]`).should('have.value', 'reporter well-worn hence');

      cy.get(`[data-cy="introductionDate"]`).type('2023-12-14T20:06');
      cy.get(`[data-cy="introductionDate"]`).blur();
      cy.get(`[data-cy="introductionDate"]`).should('have.value', '2023-12-14T20:06');

      cy.get(`[data-cy="releaseDate"]`).type('2023-12-14T16:47');
      cy.get(`[data-cy="releaseDate"]`).blur();
      cy.get(`[data-cy="releaseDate"]`).should('have.value', '2023-12-14T16:47');

      cy.get(`[data-cy="quantityIncluded"]`).type('25791.69');
      cy.get(`[data-cy="quantityIncluded"]`).should('have.value', '25791.69');

      cy.get(`[data-cy="piecesIncluded"]`).type('8601');
      cy.get(`[data-cy="piecesIncluded"]`).should('have.value', '8601');

      cy.get(`[data-cy="weight"]`).type('19395.07');
      cy.get(`[data-cy="weight"]`).should('have.value', '19395.07');

      cy.get(`[data-cy="height"]`).type('23088.95');
      cy.get(`[data-cy="height"]`).should('have.value', '23088.95');

      cy.get(`[data-cy="width"]`).type('4327');
      cy.get(`[data-cy="width"]`).should('have.value', '4327');

      cy.get(`[data-cy="depth"]`).type('32019.01');
      cy.get(`[data-cy="depth"]`).should('have.value', '32019.01');

      cy.get(`[data-cy="smallImageUrl"]`).type('yearningly when that');
      cy.get(`[data-cy="smallImageUrl"]`).should('have.value', 'yearningly when that');

      cy.get(`[data-cy="mediumImageUrl"]`).type('whoever verify');
      cy.get(`[data-cy="mediumImageUrl"]`).should('have.value', 'whoever verify');

      cy.get(`[data-cy="largeImageUrl"]`).type('minus seafood');
      cy.get(`[data-cy="largeImageUrl"]`).should('have.value', 'minus seafood');

      cy.get(`[data-cy="detailImageUrl"]`).type('duh');
      cy.get(`[data-cy="detailImageUrl"]`).should('have.value', 'duh');

      cy.get(`[data-cy="originalImageUrl"]`).type('up naturally yieldingly');
      cy.get(`[data-cy="originalImageUrl"]`).should('have.value', 'up naturally yieldingly');

      cy.get(`[data-cy="altImageUrl1"]`).type('massage');
      cy.get(`[data-cy="altImageUrl1"]`).should('have.value', 'massage');

      cy.get(`[data-cy="altImageUrl2"]`).type('reasoning');
      cy.get(`[data-cy="altImageUrl2"]`).should('have.value', 'reasoning');

      cy.get(`[data-cy="altImageUrl3"]`).type('hunger uh-huh');
      cy.get(`[data-cy="altImageUrl3"]`).should('have.value', 'hunger uh-huh');

      cy.get(`[data-cy="altImageUrl4"]`).type('monocle rim');
      cy.get(`[data-cy="altImageUrl4"]`).should('have.value', 'monocle rim');

      cy.get(`[data-cy="altImageUrl5"]`).type('chunder atop');
      cy.get(`[data-cy="altImageUrl5"]`).should('have.value', 'chunder atop');

      cy.get(`[data-cy="altImageUrl6"]`).type('abnormally');
      cy.get(`[data-cy="altImageUrl6"]`).should('have.value', 'abnormally');

      cy.get(`[data-cy="videoUrl"]`).type('phew soon');
      cy.get(`[data-cy="videoUrl"]`).should('have.value', 'phew soon');

      cy.get(`[data-cy="quantity"]`).type('29884.56');
      cy.get(`[data-cy="quantity"]`).should('have.value', '29884.56');

      cy.get(`[data-cy="price"]`).type('30268.24');
      cy.get(`[data-cy="price"]`).should('have.value', '30268.24');

      cy.get(`[data-cy="cgst"]`).type('6871.57');
      cy.get(`[data-cy="cgst"]`).should('have.value', '6871.57');

      cy.get(`[data-cy="igst"]`).type('20002.74');
      cy.get(`[data-cy="igst"]`).should('have.value', '20002.74');

      cy.get(`[data-cy="sgst"]`).type('4260.57');
      cy.get(`[data-cy="sgst"]`).should('have.value', '4260.57');

      cy.get(`[data-cy="cgstPercentage"]`).type('15428.33');
      cy.get(`[data-cy="cgstPercentage"]`).should('have.value', '15428.33');

      cy.get(`[data-cy="igstPercentage"]`).type('31550.19');
      cy.get(`[data-cy="igstPercentage"]`).should('have.value', '31550.19');

      cy.get(`[data-cy="sgstPercentage"]`).type('25124.43');
      cy.get(`[data-cy="sgstPercentage"]`).should('have.value', '25124.43');

      cy.get(`[data-cy="totalPrice"]`).type('9408.42');
      cy.get(`[data-cy="totalPrice"]`).should('have.value', '9408.42');

      cy.get(`[data-cy="description"]`).type('around wherever chubby');
      cy.get(`[data-cy="description"]`).should('have.value', 'around wherever chubby');

      cy.get(`[data-cy="longDescription"]`).type('creator train');
      cy.get(`[data-cy="longDescription"]`).should('have.value', 'creator train');

      cy.get(`[data-cy="info"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="info"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isVirtual"]`).should('not.be.checked');
      cy.get(`[data-cy="isVirtual"]`).click();
      cy.get(`[data-cy="isVirtual"]`).should('be.checked');

      cy.get(`[data-cy="isVariant"]`).should('not.be.checked');
      cy.get(`[data-cy="isVariant"]`).click();
      cy.get(`[data-cy="isVariant"]`).should('be.checked');

      cy.get(`[data-cy="requireInventory"]`).should('not.be.checked');
      cy.get(`[data-cy="requireInventory"]`).click();
      cy.get(`[data-cy="requireInventory"]`).should('be.checked');

      cy.get(`[data-cy="returnable"]`).should('not.be.checked');
      cy.get(`[data-cy="returnable"]`).click();
      cy.get(`[data-cy="returnable"]`).should('be.checked');

      cy.get(`[data-cy="isPopular"]`).should('not.be.checked');
      cy.get(`[data-cy="isPopular"]`).click();
      cy.get(`[data-cy="isPopular"]`).should('be.checked');

      cy.get(`[data-cy="isBanner"]`).should('not.be.checked');
      cy.get(`[data-cy="isBanner"]`).click();
      cy.get(`[data-cy="isBanner"]`).should('be.checked');

      cy.get(`[data-cy="bannerUrl"]`).type('qua gadzooks');
      cy.get(`[data-cy="bannerUrl"]`).should('have.value', 'qua gadzooks');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        product = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productPageUrl);
    });
  });
});
