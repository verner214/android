--------------------
här-onCreate:
  hämta bild motsvarande uri

här-sparaklick:
  anropa formmetod som sparar bilder och text. återvänd som i BrygdEditFragment.


--------------------
i BrygdFragment:
  vid klick på ny galleryitem -> starta intent som väljer bild och returnerar uri.
  
onActivityResult
  från bildväljare ovan:  
    om uri inte null -> starta BrygdGalleryNewActivity via intent och sätt Uri som intent parameter
  från BrygdGalleryNewActivity:
    gör som vid ok från BrygdEditActivity.
	
	
------------------	
sen:
1. visa gallery,
2. pager med bildfragments
3. meny för edit och edit.
4. roterande fragment ovan.
